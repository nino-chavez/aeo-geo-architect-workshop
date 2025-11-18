-- ============================================================================
-- FordDirect Dealer Entity Registry - Database Schema
-- PostgreSQL 16+ with PostGIS extension
-- ============================================================================
-- Purpose: Store data for 3,200 Ford/Lincoln dealers to power AI search
-- ============================================================================

-- === EXTENSIONS ===

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";  -- For fuzzy string matching

-- === HELPER FUNCTIONS ===

-- Function to update 'updated_at' timestamp automatically
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- === CORE TABLES ===

-- Dealership Groups (corporate entities owning multiple dealers)
CREATE TABLE dealership_groups (
    group_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    group_name VARCHAR(255) NOT NULL,
    headquarters_city VARCHAR(100),
    headquarters_state CHAR(2),
    total_locations INT DEFAULT 0,
    website_url TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_dealership_groups_name ON dealership_groups(group_name);

-- Dealers
CREATE TABLE dealers (
    dealer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id UUID REFERENCES dealership_groups(group_id) ON DELETE SET NULL,

    -- Basic Info
    legal_name VARCHAR(255) NOT NULL,
    dba_name VARCHAR(255),
    dealer_code VARCHAR(20) UNIQUE NOT NULL,
    dealer_type VARCHAR(30) CHECK (dealer_type IN (
        'metro',
        'rural',
        'specialty',
        'dealership_group',
        'ev_focused'
    )),

    -- Contact
    website_url TEXT,
    phone VARCHAR(20),
    email VARCHAR(255),
    social_media JSONB,  -- {"facebook": "url", "twitter": "url", "instagram": "url"}

    -- Attributes
    brands JSONB,  -- ["Ford", "Lincoln"]
    is_active BOOLEAN DEFAULT true,

    -- Audit
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),

    -- Full-text search
    search_vector tsvector GENERATED ALWAYS AS (
        to_tsvector('english',
            coalesce(legal_name, '') || ' ' ||
            coalesce(dba_name, '') || ' ' ||
            coalesce(dealer_code, '')
        )
    ) STORED
);

-- Indexes for dealers
CREATE INDEX idx_dealers_group ON dealers(group_id);
CREATE INDEX idx_dealers_type ON dealers(dealer_type);
CREATE INDEX idx_dealers_search ON dealers USING GIN(search_vector);
CREATE INDEX idx_dealers_active ON dealers(is_active);
CREATE INDEX idx_dealers_code ON dealers(dealer_code);

-- Trigger for dealers.updated_at
CREATE TRIGGER update_dealers_modtime
BEFORE UPDATE ON dealers
FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- Locations (dealers can have multiple physical locations)
CREATE TABLE locations (
    location_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    dealer_id UUID NOT NULL REFERENCES dealers(dealer_id) ON DELETE CASCADE,

    -- Address
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state CHAR(2) NOT NULL,
    zip VARCHAR(10) NOT NULL,
    country CHAR(2) DEFAULT 'US',

    -- Geospatial
    geo_point GEOGRAPHY(POINT, 4326),  -- PostGIS: lat/long for proximity search

    -- Metadata
    is_primary BOOLEAN DEFAULT false,  -- Primary location for dealer
    hours JSONB,  -- {"sales": {"monday": {"open": "08:00", "close": "20:00"}, ...}, "service": {...}}
    amenities JSONB,  -- ["WiFi", "Customer Lounge", "Service Shuttle", "EV Charging"]

    -- Audit
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for locations
CREATE INDEX idx_locations_dealer ON locations(dealer_id);
CREATE INDEX idx_locations_geo ON locations USING GIST(geo_point);
CREATE INDEX idx_locations_city_state ON locations(city, state);
CREATE INDEX idx_locations_zip ON locations(zip);
CREATE INDEX idx_locations_primary ON locations(is_primary);

-- Vehicle Specifications (reference data from OEM)
CREATE TABLE vehicle_specs (
    spec_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Basic Info
    year INT NOT NULL,
    make VARCHAR(50) NOT NULL CHECK (make IN ('Ford', 'Lincoln')),
    model VARCHAR(100) NOT NULL,
    trim VARCHAR(100),
    body_type VARCHAR(50),  -- Truck, SUV, Sedan, Coupe, etc.

    -- Performance
    drivetrain VARCHAR(20),  -- 4WD, AWD, RWD, FWD
    engine VARCHAR(100),
    fuel_capacity_gallons DECIMAL(5, 1),
    mpg_city INT,
    mpg_highway INT,
    towing_capacity_lbs INT,
    payload_capacity_lbs INT,
    seating_capacity INT,

    -- Features
    standard_features JSONB,  -- ["Cruise Control", "Bluetooth", ...]
    available_options JSONB,  -- ["Tow Package", "Sunroof", ...]

    -- Pricing
    base_msrp DECIMAL(10, 2),

    -- Audit
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for vehicle_specs
CREATE INDEX idx_vehicle_specs_year_make_model ON vehicle_specs(year, make, model);
CREATE INDEX idx_vehicle_specs_make ON vehicle_specs(make);
CREATE INDEX idx_vehicle_specs_body_type ON vehicle_specs(body_type);

-- Inventory (actual vehicles in stock at dealers)
CREATE TABLE inventory (
    vin VARCHAR(17) PRIMARY KEY,  -- VIN is unique globally
    dealer_id UUID NOT NULL REFERENCES dealers(dealer_id) ON DELETE CASCADE,
    spec_id UUID REFERENCES vehicle_specs(spec_id) ON DELETE SET NULL,

    -- Vehicle Details
    year INT NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(100) NOT NULL,
    trim VARCHAR(100),
    exterior_color VARCHAR(50),
    interior_color VARCHAR(50),

    -- Pricing
    msrp DECIMAL(10, 2),
    dealer_price DECIMAL(10, 2),
    discount_amount DECIMAL(10, 2) GENERATED ALWAYS AS (msrp - dealer_price) STORED,

    -- Status
    status VARCHAR(20) CHECK (status IN ('in_stock', 'in_transit', 'sold', 'reserved')),
    status_updated_at TIMESTAMP DEFAULT NOW(),
    arrival_date TIMESTAMP,
    days_in_inventory INT GENERATED ALWAYS AS (
        EXTRACT(DAY FROM (NOW() - arrival_date))
    ) STORED,

    -- Features & Media
    features JSONB,  -- Specific features for this VIN
    images JSONB,  -- ["url1", "url2", ...]

    -- Audit
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for inventory
CREATE INDEX idx_inventory_dealer ON inventory(dealer_id);
CREATE INDEX idx_inventory_spec ON inventory(spec_id);
CREATE INDEX idx_inventory_status ON inventory(status);
CREATE INDEX idx_inventory_make_model ON inventory(make, model);
CREATE INDEX idx_inventory_year ON inventory(year);
CREATE INDEX idx_inventory_price ON inventory(dealer_price);
CREATE INDEX idx_inventory_features ON inventory USING GIN(features);

-- Trigger for inventory status updates
CREATE OR REPLACE FUNCTION update_inventory_status_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.status IS DISTINCT FROM NEW.status THEN
        NEW.status_updated_at = NOW();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_inventory_status_modtime
BEFORE UPDATE ON inventory
FOR EACH ROW
EXECUTE FUNCTION update_inventory_status_timestamp();

CREATE TRIGGER update_inventory_modtime
BEFORE UPDATE ON inventory
FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- Reviews (from Google, Yelp, DealerRater, etc.)
CREATE TABLE reviews (
    review_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    dealer_id UUID NOT NULL REFERENCES dealers(dealer_id) ON DELETE CASCADE,

    -- Source
    source VARCHAR(50) CHECK (source IN ('google', 'yelp', 'dealerrater', 'edmunds', 'facebook', 'cargurus')),
    external_id VARCHAR(255),  -- Review ID from source

    -- Review Content
    rating DECIMAL(2, 1) CHECK (rating >= 1.0 AND rating <= 5.0),
    review_text TEXT,
    author_name VARCHAR(255),
    review_date DATE,

    -- Dealer Response
    dealer_response TEXT,
    response_date DATE,

    -- Metadata
    metadata JSONB,  -- Source-specific data

    -- Audit
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for reviews
CREATE INDEX idx_reviews_dealer ON reviews(dealer_id);
CREATE INDEX idx_reviews_rating ON reviews(rating);
CREATE INDEX idx_reviews_source ON reviews(source);
CREATE INDEX idx_reviews_date ON reviews(review_date);

-- Service Capabilities
CREATE TABLE service_capabilities (
    capability_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    dealer_id UUID NOT NULL REFERENCES dealers(dealer_id) ON DELETE CASCADE,

    -- Service Type
    service_type VARCHAR(50) CHECK (service_type IN (
        'maintenance',
        'collision',
        'ev_charging',
        'commercial_fleet',
        'body_shop',
        'parts',
        'tire_center'
    )),

    -- Certification
    is_certified BOOLEAN DEFAULT false,
    certification_level VARCHAR(20),  -- Bronze, Silver, Gold, Platinum

    -- Details
    description TEXT,
    equipment JSONB,  -- ["Hydraulic Lift", "Diagnostic Computer", "Level 3 EV Charger"]
    hours JSONB,  -- Service hours (may differ from sales)

    -- Audit
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for service_capabilities
CREATE INDEX idx_service_capabilities_dealer ON service_capabilities(dealer_id);
CREATE INDEX idx_service_capabilities_type ON service_capabilities(service_type);
CREATE INDEX idx_service_capabilities_certified ON service_capabilities(is_certified);

-- Special Offerings (fleet sales, EV infrastructure, etc.)
CREATE TABLE special_offerings (
    offering_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    dealer_id UUID NOT NULL REFERENCES dealers(dealer_id) ON DELETE CASCADE,

    -- Offering Type
    offering_type VARCHAR(50) CHECK (offering_type IN (
        'fleet_sales',
        'commercial_upfitting',
        'ev_infrastructure',
        'rental',
        'trade_in_program',
        'mobility_solutions',
        'business_center'
    )),

    -- Details
    name VARCHAR(255),
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    details JSONB,  -- Type-specific metadata

    -- Audit
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for special_offerings
CREATE INDEX idx_special_offerings_dealer ON special_offerings(dealer_id);
CREATE INDEX idx_special_offerings_type ON special_offerings(offering_type);
CREATE INDEX idx_special_offerings_active ON special_offerings(is_active);

-- Financing Options
CREATE TABLE financing_options (
    financing_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    dealer_id UUID NOT NULL REFERENCES dealers(dealer_id) ON DELETE CASCADE,

    -- Program Info
    program_name VARCHAR(255) NOT NULL,
    program_type VARCHAR(30) CHECK (program_type IN ('lease', 'finance', 'cash_incentive', 'loyalty_program')),

    -- Terms
    min_apr DECIMAL(5, 2),
    max_apr DECIMAL(5, 2),
    min_term_months INT,
    max_term_months INT,
    eligibility_requirements TEXT,

    -- Validity
    valid_from DATE,
    valid_through DATE,

    -- Audit
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for financing_options
CREATE INDEX idx_financing_options_dealer ON financing_options(dealer_id);
CREATE INDEX idx_financing_options_type ON financing_options(program_type);
CREATE INDEX idx_financing_options_valid ON financing_options(valid_from, valid_through);

-- === VIEWS FOR COMMON QUERIES ===

-- Active dealers with primary location
CREATE VIEW dealers_with_primary_location AS
SELECT
    d.dealer_id,
    d.legal_name,
    d.dba_name,
    d.dealer_code,
    d.dealer_type,
    d.phone,
    d.website_url,
    d.brands,
    l.address_line1,
    l.city,
    l.state,
    l.zip,
    l.geo_point,
    l.hours,
    l.amenities
FROM dealers d
LEFT JOIN locations l ON d.dealer_id = l.dealer_id AND l.is_primary = true
WHERE d.is_active = true;

-- Dealer aggregate ratings
CREATE VIEW dealer_aggregate_ratings AS
SELECT
    dealer_id,
    COUNT(*) AS total_reviews,
    AVG(rating) AS avg_rating,
    MAX(review_date) AS most_recent_review
FROM reviews
GROUP BY dealer_id;

-- In-stock inventory summary
CREATE VIEW inventory_summary AS
SELECT
    dealer_id,
    make,
    model,
    COUNT(*) AS count,
    MIN(dealer_price) AS min_price,
    MAX(dealer_price) AS max_price,
    AVG(dealer_price) AS avg_price
FROM inventory
WHERE status = 'in_stock'
GROUP BY dealer_id, make, model;

-- === SAMPLE DATA (for testing) ===

-- Insert a sample dealership group
INSERT INTO dealership_groups (group_name, headquarters_city, headquarters_state, total_locations)
VALUES ('AutoNation Ford', 'Fort Lauderdale', 'FL', 15);

-- Insert a sample dealer
INSERT INTO dealers (legal_name, dba_name, dealer_code, dealer_type, phone, website_url, brands)
VALUES (
    'Aurora Ford Lincoln LLC',
    'Aurora Ford',
    'CO-AUR-001',
    'metro',
    '+1-720-555-0100',
    'https://auroraford.com',
    '["Ford", "Lincoln"]'::jsonb
);

-- Insert a primary location for the dealer
INSERT INTO locations (
    dealer_id,
    address_line1,
    city,
    state,
    zip,
    geo_point,
    is_primary,
    hours,
    amenities
)
VALUES (
    (SELECT dealer_id FROM dealers WHERE dealer_code = 'CO-AUR-001'),
    '14000 E Iliff Ave',
    'Aurora',
    'CO',
    '80014',
    ST_SetSRID(ST_MakePoint(-104.8194, 39.6703), 4326)::geography,
    true,
    '{"sales": {"monday": {"open": "08:00", "close": "20:00"}, "saturday": {"open": "08:00", "close": "18:00"}}, "service": {"monday": {"open": "07:00", "close": "18:00"}}}'::jsonb,
    '["WiFi", "Customer Lounge", "Service Shuttle", "EV Charging"]'::jsonb
);

-- === COMMENTS FOR DOCUMENTATION ===

COMMENT ON TABLE dealers IS 'Main dealer entity - represents a Ford/Lincoln dealership';
COMMENT ON TABLE locations IS 'Physical locations for dealers (dealers can have multiple locations)';
COMMENT ON TABLE inventory IS 'Vehicles currently in stock or in-transit at dealers';
COMMENT ON TABLE vehicle_specs IS 'Reference data for vehicle specifications from OEM';
COMMENT ON TABLE reviews IS 'Customer reviews from multiple sources (Google, Yelp, etc.)';
COMMENT ON TABLE service_capabilities IS 'Services offered by dealers (maintenance, EV charging, etc.)';
COMMENT ON TABLE special_offerings IS 'Special programs like fleet sales, upfitting, etc.';
COMMENT ON TABLE financing_options IS 'Financing and leasing programs available at dealers';

-- === GRANT PERMISSIONS (adjust as needed) ===

-- Create application user (if needed)
-- CREATE USER forddirect_api WITH PASSWORD 'secure_password';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO forddirect_api;
-- GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO forddirect_api;
