# Exercise 2: Schema.org Templates
## Create JSON-LD for Five Dealer Types

**Duration**: 4 hours (Day 2)
**Squad**: Squad C (Schema Specialists)
**Deliverables**: 5 validated JSON-LD templates + documentation

---

## 🎯 Objective

Create production-ready Schema.org JSON-LD templates for five dealer archetypes. These templates will be used to generate structured data for all 3,200 FordDirect dealers, making them visible and parseable by AI search engines.

**Success Criteria**:
- ✅ 5 complete templates (Metro, Rural, Specialty, Dealership Group, EV-Focused)
- ✅ All templates pass Google Rich Results Test
- ✅ Template Selection Guide created
- ✅ Implementation Guide for FordDirect developers

---

## 📋 The Five Dealer Archetypes

### **1. Metro Dealer**
**Profile**: High-volume urban dealership
- Large inventory (100+ vehicles)
- Extended hours (open late weekdays, weekends)
- Multiple departments (sales, service, parts, collision)
- High review count (100+)
- Premium amenities

**Example**: Chicago Ford, LA Lincoln, Houston Ford

### **2. Rural Dealer**
**Profile**: Small-town community dealership
- Smaller inventory (20-40 vehicles)
- Personal service emphasis
- Local community ties
- Service-focused (maintenance, parts)
- Family-owned heritage

**Example**: Cheyenne Ford, Boise Lincoln, Rural Montana dealers

### **3. Specialty Dealer**
**Profile**: Commercial/fleet specialist
- Focus on trucks and commercial vehicles
- Upfitting services (utility beds, plows, etc.)
- Fleet sales program
- Business customer focus
- Commercial financing

**Example**: Texas Commercial Trucks, Ohio Fleet Sales

### **4. Dealership Group**
**Profile**: Multi-location corporate entity
- Part of larger dealer group (AutoNation, Lithia, etc.)
- Shared services across locations
- Corporate structure
- Multiple brands/locations
- Centralized operations

**Example**: AutoNation Ford (15 locations), Lithia Lincoln

### **5. EV-Focused Dealer**
**Profile**: Electric vehicle specialist
- F-150 Lightning inventory
- Mustang Mach-E focus
- EV charging infrastructure
- EV-certified technicians
- Sustainability messaging

**Example**: California Ford (EV leader), Portland Lincoln EV

---

## 🛠️ Technical Requirements

### **Schema.org Types to Use**

**Primary**:
- `LocalBusiness` - Base type for all dealers
- `AutomotiveBusiness` - Specialized type for car dealers

**Additional** (as needed):
- `AutoDealer` - For sales departments
- `AutoRepair` - For service departments
- `AutoPartsStore` - For parts departments
- `Product` → `Vehicle` - For inventory
- `Offer` - For vehicle pricing/availability

### **Required Properties** (All Templates)

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `@context` | String | ✅ | `https://schema.org` |
| `@type` | Array | ✅ | `["LocalBusiness", "AutomotiveBusiness"]` |
| `@id` | URL | ✅ | Canonical URL for dealer |
| `name` | String | ✅ | Legal or DBA name |
| `address` | PostalAddress | ✅ | Full address |
| `geo` | GeoCoordinates | ✅ | Lat/long |
| `telephone` | String | ✅ | Phone number (+1 format) |
| `url` | URL | ✅ | Dealer website |
| `openingHoursSpecification` | Array | ✅ | Hours by day/department |
| `aggregateRating` | AggregateRating | Recommended | Reviews summary |
| `priceRange` | String | Recommended | `$$-$$$` |

### **Recommended Properties**

| Property | Purpose |
|----------|---------|
| `description` | SEO-friendly summary of dealer |
| `areaServed` | Geographic service area (GeoCircle) |
| `makesOffer` | Featured inventory (1-3 vehicles) |
| `hasOfferCatalog` | Aggregate inventory types |
| `amenityFeature` | WiFi, lounge, shuttle, EV charging |
| `department` | Separate sales, service, parts |
| `potentialAction` | Appointment scheduling, trade-in |
| `sameAs` | Social media URLs |

---

## 📁 Deliverables

### **1. Five JSON-LD Templates**

**Files** (in `/solution/`):
1. `metro-dealer.json` - Complete template with max properties
2. `rural-dealer.json` - Emphasis on community, service
3. `specialty-dealer.json` - Commercial focus, upfitting
4. `dealership-group.json` - Corporate structure, multi-location
5. `ev-focused-dealer.json` - EV infrastructure, sustainability

**Each template must**:
- ✅ Be valid JSON-LD
- ✅ Pass Google Rich Results Test
- ✅ Include comments explaining customization points
- ✅ Use variables for dealer-specific data (e.g., `{{dealer.name}}`)

### **2. Template Selection Guide**

**File**: `solution/TEMPLATE_GUIDE.md`

**Contents**:
- Decision tree: Which template for which dealer?
- Comparison table of template differences
- Customization instructions
- Examples of each archetype

### **3. Implementation Guide**

**File**: `solution/IMPLEMENTATION_GUIDE.md`

**Contents**:
- Where to place JSON-LD in HTML
- Variable substitution strategy
- Inventory update frequency recommendations
- Validation checklist
- Common pitfalls

### **4. Validation Reports**

**File**: `solution/validation-results.md`

**Contents**:
- Google Rich Results Test screenshots
- Schema.org validator results
- Warnings and how to fix them
- Performance recommendations

---

## 🚀 Getting Started

### **Step 1: Study Schema.org**

Read these pages:
- [LocalBusiness](https://schema.org/LocalBusiness)
- [AutomotiveBusiness](https://schema.org/AutomotiveBusiness)
- [Vehicle](https://schema.org/Vehicle)
- [Offer](https://schema.org/Offer)
- [OpeningHoursSpecification](https://schema.org/OpeningHoursSpecification)

### **Step 2: Analyze Competitor Schemas**

Visit 3-5 automotive dealer websites and inspect their JSON-LD:
1. Right-click → View Page Source
2. Search for `<script type="application/ld+json">`
3. Copy their schema to study

**Good examples**:
- BMW dealers (comprehensive schemas)
- Tesla stores (clean structure)
- Large dealership groups (AutoNation, Lithia)

### **Step 3: Build Metro Template First**

The Metro template is the most comprehensive. Start here, then simplify for other archetypes.

**Workflow**:
1. Create basic structure (`@context`, `@type`, `name`, `address`)
2. Add required properties
3. Add recommended properties
4. Add inventory (1-3 vehicles)
5. Validate with Google Rich Results Test
6. Iterate until PASS

### **Step 4: Adapt for Other Archetypes**

**Rural**: Remove some amenities, reduce inventory, emphasize service
**Specialty**: Add commercial features, upfitting services
**Dealership Group**: Add `parentOrganization`, multiple `location`
**EV-Focused**: Add EV charging, sustainability features

---

## 💡 Hints

<details>
<summary><strong>Hint 1: Hours Format</strong></summary>

Use ISO 8601 time format (24-hour):

```json
{
  "@type": "OpeningHoursSpecification",
  "dayOfWeek": ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"],
  "opens": "08:00",
  "closes": "20:00"
}
```

**Multiple hour sets** (sales vs service):
```json
{
  "department": [
    {
      "@type": "AutoDealer",
      "name": "Sales Department",
      "openingHours": "Mo-Fr 08:00-20:00, Sa 08:00-18:00"
    },
    {
      "@type": "AutoRepair",
      "name": "Service Department",
      "openingHours": "Mo-Fr 07:00-18:00, Sa 07:00-16:00"
    }
  ]
}
```
</details>

<details>
<summary><strong>Hint 2: Inventory Representation</strong></summary>

**Don't include ALL inventory** - just 1-3 featured vehicles:

```json
{
  "makesOffer": [
    {
      "@type": "Offer",
      "itemOffered": {
        "@type": "Vehicle",
        "brand": {"@type": "Brand", "name": "Ford"},
        "model": "F-150",
        "vehicleConfiguration": "Lariat",
        "vehicleModelDate": "2024",
        "sku": "VIN_HERE",
        "color": "White Platinum Metallic"
      },
      "price": "54995",
      "priceCurrency": "USD",
      "availability": "https://schema.org/InStock"
    }
  ],
  "hasOfferCatalog": {
    "@type": "OfferCatalog",
    "name": "New Ford Inventory",
    "itemListElement": [
      {"@type": "Offer", "itemOffered": {"@type": "Vehicle", "model": "F-150"}},
      {"@type": "Offer", "itemOffered": {"@type": "Vehicle", "model": "Mustang"}}
    ]
  }
}
```

Use `makesOffer` for specific vehicles, `hasOfferCatalog` for aggregate.
</details>

<details>
<summary><strong>Hint 3: Area Served (Geospatial)</strong></summary>

Define service area with `GeoCircle`:

```json
{
  "areaServed": {
    "@type": "GeoCircle",
    "geoMidpoint": {
      "@type": "GeoCoordinates",
      "latitude": 39.6703,
      "longitude": -104.8194
    },
    "geoRadius": "50 miles"
  }
}
```

This tells AI: "This dealer serves customers within 50 miles of this location."
</details>

<details>
<summary><strong>Hint 4: Reviews & Ratings</strong></summary>

Aggregate reviews from all sources:

```json
{
  "aggregateRating": {
    "@type": "AggregateRating",
    "ratingValue": 4.6,
    "reviewCount": 342,
    "bestRating": 5,
    "worstRating": 1
  }
}
```

**DO NOT** include individual reviews in JSON-LD (too verbose). Only aggregate.
</details>

<details>
<summary><strong>Hint 5: Variable Substitution</strong></summary>

Use templating syntax for dynamic values:

```json
{
  "name": "{{dealer.legal_name}}",
  "telephone": "{{dealer.phone}}",
  "address": {
    "@type": "PostalAddress",
    "streetAddress": "{{location.address_line1}}",
    "addressLocality": "{{location.city}}",
    "addressRegion": "{{location.state}}",
    "postalCode": "{{location.zip}}"
  }
}
```

Document which variables map to which database fields.
</details>

---

## ✅ Validation Checklist

Before considering templates complete:

### **All Templates**
- [ ] Valid JSON syntax (use JSONLint.com)
- [ ] Passes Google Rich Results Test
- [ ] Includes `@context`, `@type`, `@id`
- [ ] All required properties present
- [ ] Phone in E.164 format (+1-xxx-xxx-xxxx)
- [ ] URLs are absolute (not relative)
- [ ] Hours use 24-hour format
- [ ] Geo coordinates are accurate

### **Per Template**
- [ ] Metro: 100+ features, extended hours, multiple departments
- [ ] Rural: Personal touch, community focus, service emphasis
- [ ] Specialty: Commercial features, fleet sales, upfitting
- [ ] Dealership Group: `parentOrganization`, multiple locations
- [ ] EV-Focused: EV charging amenities, sustainability

### **Documentation**
- [ ] Template Guide explains when to use each
- [ ] Implementation Guide has code examples
- [ ] Validation results documented with screenshots

---

## 🤝 Integration with Other Squads

### **Squad A Needs From You**:
- Template structure (what properties exist?)
- Database field mapping (which DB columns → which Schema properties?)

### **You Need From Squad A**:
- Dealer data model (what fields are available?)
- API endpoint: `GET /api/dealers/{id}` (to test dynamic generation)

**Integration Meeting**: End of Day 2

---

## 📖 Resources

**Schema.org**:
- [Full Documentation](https://schema.org/)
- [LocalBusiness](https://schema.org/LocalBusiness)
- [Vehicle](https://schema.org/Vehicle)

**Google Guidelines**:
- [Structured Data Policies](https://developers.google.com/search/docs/appearance/structured-data/sd-policies)
- [Local Business](https://developers.google.com/search/docs/appearance/structured-data/local-business)

**Validation Tools**:
- [Google Rich Results Test](https://search.google.com/test/rich-results)
- [Schema Markup Validator](https://validator.schema.org/)
- [JSON-LD Playground](https://json-ld.org/playground/)

---

## 🎯 Success Metrics

**MVP** (Day 2 end):
- 3 templates complete and validated (Metro, Rural, Specialty)
- Basic Template Guide

**Target** (Day 3 end):
- All 5 templates complete
- Full documentation
- Validation screenshots

**Stretch**:
- Template generator tool (web form → JSON-LD)
- A/B testing recommendations

---

**Good luck, Schema Specialists! You're teaching AI how to read dealers. 📋**
