# Squad C: Schema Specialists
## Project 3200 - FordDirect AI Search Workshop

**Mission**: Create JSON-LD templates that make Ford/Lincoln dealers machine-readable to AI search engines. You're translating business reality into structured data that LLMs understand.

---

## 🎯 Your Objective

**Build Schema.org templates that answer**:
> "When Google AI Overviews or ChatGPT needs information about a Ford dealer, what structured data must exist? How is it formatted?"

**Why This Matters**:
- LLMs read structured data (JSON-LD), not HTML/CSS
- 3,200 dealers need standardized templates (not custom markup)
- Schema.org is the "language" AI search engines speak
- Your templates become the production implementation guide

---

## 👥 Team Composition

**Squad Size**: 2-3 people

**Required Skills**:
- JSON proficiency
- Attention to detail
- HTML/markup understanding (helpful)
- Technical writing

**Nice to Have** (not required):
- Schema.org experience
- SEO background
- Frontend development

**NOT Required**:
- Backend development (Python/Java)
- Database knowledge
- LLM expertise

---

## 📦 What You'll Build

### **Deliverable #1: 5 Dealer Templates**

**Files** (in `solution/`):
1. `metro-dealer.json` - High-volume urban dealer
2. `rural-dealer.json` - Single-location community dealer
3. `specialty-dealer.json` - Commercial/fleet focus
4. `dealership-group.json` - Multi-location corporate
5. `ev-focused-dealer.json` - F-150 Lightning, EV services

**Each template must include**:
- `@context`: Schema.org
- `@type`: LocalBusiness + AutomotiveBusiness
- Core properties:
  - name, address, phone, hours
  - geo coordinates
  - reviews/ratings
  - services offered
- Vehicle inventory (Product + Offer)
- Unique differentiators

---

### **Example: Metro Dealer Template**

```json
{
  "@context": "https://schema.org",
  "@type": ["LocalBusiness", "AutomotiveBusiness"],
  "@id": "https://dealers.ford.com/aurora-ford",
  "name": "Aurora Ford Lincoln",
  "alternateName": "Aurora Ford",
  "description": "Full-service Ford and Lincoln dealership serving the greater Denver area with new & used vehicles, parts, and service. Specializing in F-150 and Super Duty trucks.",

  "address": {
    "@type": "PostalAddress",
    "streetAddress": "14000 E Iliff Ave",
    "addressLocality": "Aurora",
    "addressRegion": "CO",
    "postalCode": "80014",
    "addressCountry": "US"
  },

  "geo": {
    "@type": "GeoCoordinates",
    "latitude": 39.6703,
    "longitude": -104.8194
  },

  "telephone": "+1-720-555-0100",
  "url": "https://auroraford.com",

  "openingHoursSpecification": [
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"],
      "opens": "08:00",
      "closes": "20:00"
    },
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Saturday",
      "opens": "08:00",
      "closes": "18:00"
    },
    {
      "@type": "OpeningHoursSpecification",
      "dayOfWeek": "Sunday",
      "opens": "10:00",
      "closes": "17:00"
    }
  ],

  "aggregateRating": {
    "@type": "AggregateRating",
    "ratingValue": 4.6,
    "reviewCount": 342,
    "bestRating": 5,
    "worstRating": 1
  },

  "priceRange": "$$-$$$",

  "areaServed": {
    "@type": "GeoCircle",
    "geoMidpoint": {
      "@type": "GeoCoordinates",
      "latitude": 39.6703,
      "longitude": -104.8194
    },
    "geoRadius": "50 miles"
  },

  "makesOffer": [
    {
      "@type": "Offer",
      "itemOffered": {
        "@type": "Vehicle",
        "brand": {
          "@type": "Brand",
          "name": "Ford"
        },
        "model": "F-150",
        "vehicleConfiguration": "Lariat",
        "vehicleModelDate": "2024",
        "sku": "1FTFW1E50PFA12345",
        "color": "White Platinum Metallic",
        "bodyType": "Crew Cab Pickup",
        "driveWheelConfiguration": "Four-wheel drive",
        "fuelType": "Gasoline",
        "vehicleEngine": {
          "@type": "EngineSpecification",
          "name": "3.5L EcoBoost V6"
        },
        "mileageFromOdometer": {
          "@type": "QuantitativeValue",
          "value": 12,
          "unitCode": "SMI"
        }
      },
      "price": "54995",
      "priceCurrency": "USD",
      "availability": "https://schema.org/InStock",
      "inventoryLevel": {
        "@type": "QuantitativeValue",
        "value": 1
      },
      "validThrough": "2024-12-31"
    }
  ],

  "hasOfferCatalog": {
    "@type": "OfferCatalog",
    "name": "New Ford Inventory",
    "itemListElement": [
      {
        "@type": "Offer",
        "itemOffered": {
          "@type": "Vehicle",
          "model": "F-150"
        }
      },
      {
        "@type": "Offer",
        "itemOffered": {
          "@type": "Vehicle",
          "model": "Mustang"
        }
      },
      {
        "@type": "Offer",
        "itemOffered": {
          "@type": "Vehicle",
          "model": "Explorer"
        }
      }
    ]
  },

  "amenityFeature": [
    {
      "@type": "LocationFeatureSpecification",
      "name": "Customer Lounge",
      "value": true
    },
    {
      "@type": "LocationFeatureSpecification",
      "name": "Free WiFi",
      "value": true
    },
    {
      "@type": "LocationFeatureSpecification",
      "name": "Service Shuttle",
      "value": true
    }
  ],

  "potentialAction": [
    {
      "@type": "ScheduleAction",
      "name": "Schedule Service Appointment",
      "target": {
        "@type": "EntryPoint",
        "urlTemplate": "https://auroraford.com/service/schedule",
        "actionPlatform": [
          "http://schema.org/DesktopWebPlatform",
          "http://schema.org/MobileWebPlatform"
        ]
      }
    },
    {
      "@type": "TradeAction",
      "name": "Request Trade-In Value",
      "target": {
        "@type": "EntryPoint",
        "urlTemplate": "https://auroraford.com/trade-in",
        "actionPlatform": [
          "http://schema.org/DesktopWebPlatform",
          "http://schema.org/MobileWebPlatform"
        ]
      }
    }
  ],

  "department": [
    {
      "@type": "AutoDealer",
      "name": "New Vehicle Sales",
      "telephone": "+1-720-555-0101"
    },
    {
      "@type": "AutoDealer",
      "name": "Used Vehicle Sales",
      "telephone": "+1-720-555-0102"
    },
    {
      "@type": "AutoRepair",
      "name": "Service Department",
      "telephone": "+1-720-555-0103",
      "openingHours": "Mo-Fr 07:00-18:00, Sa 07:00-16:00"
    },
    {
      "@type": "AutoPartsStore",
      "name": "Parts Department",
      "telephone": "+1-720-555-0104"
    }
  ],

  "sameAs": [
    "https://www.facebook.com/AuroraFord",
    "https://twitter.com/AuroraFord",
    "https://www.instagram.com/auroraford"
  ]
}
```

---

### **Deliverable #2: Template Selection Guide**
**File**: `TEMPLATE_GUIDE.md`

**Purpose**: Help FordDirect determine which template fits which dealer

**Content**:

| Template | Use When | Key Differentiators | Examples |
|----------|----------|---------------------|----------|
| **Metro Dealer** | Urban, high volume, multi-brand | Large inventory, extended hours, multiple departments | Chicago Ford, LA Lincoln |
| **Rural Dealer** | Small town, community-focused | Personal service, local ties, service emphasis | Cheyenne Ford, Boise Lincoln |
| **Specialty Dealer** | Commercial/fleet focus | Business customers, upfitting, fleet sales | Texas Commercial Trucks |
| **Dealership Group** | Multi-location corporate | Shared services, corporate structure | AutoNation Ford group |
| **EV-Focused Dealer** | F-150 Lightning, Mustang Mach-E | EV charging, EV-certified techs, sustainability | California EV dealers |

---

### **Deliverable #3: Validation Reports**
**File**: `validation-results.md`

**Purpose**: Prove templates pass Google Rich Results Test

**For each template**:
1. Screenshot of Google Rich Results Test (PASS)
2. List of warnings (if any) and how to fix
3. Schema.org validator results
4. JSON-LD Playground link

**Example**:
```markdown
## Metro Dealer Template - Validation Results

### Google Rich Results Test
- **Status**: ✅ PASS
- **Eligible Rich Results**:
  - Local Business
  - Product (Vehicle inventory)
  - Organization
- **Screenshot**: `validation-screenshots/metro-dealer-google.png`

### Warnings
- ⚠️ `priceRange` is recommended but not required
- ⚠️ `logo` property missing (add for Organization rich result)

### Recommendations
1. Add `logo` property (dealer logo URL)
2. Add more specific `priceRange` (e.g., "$30,000-$80,000")
3. Include more inventory items (at least 5 vehicles)

### Tools Used
- [Google Rich Results Test](https://search.google.com/test/rich-results)
- [Schema.org Validator](https://validator.schema.org/)
- [JSON-LD Playground](https://json-ld.org/playground/)
```

---

### **Deliverable #4: Implementation Guide**
**File**: `IMPLEMENTATION_GUIDE.md`

**Purpose**: Give FordDirect developers step-by-step instructions

**Content**:
1. **Where to Place JSON-LD**
   - Option A: `<script type="application/ld+json">` in `<head>`
   - Option B: External file loaded dynamically
   - Recommendation: Inline for critical data, external for inventory

2. **Variable Substitution**
   - Show template variables: `{{dealer.name}}`, `{{dealer.phone}}`
   - Provide templating engine examples (Jinja2, Handlebars)

3. **Inventory Updates**
   - Real-time: Update JSON-LD when inventory changes
   - Batch: Regenerate nightly for all 3,200 dealers
   - Recommendation: Hybrid (critical dealers real-time, others batch)

4. **Testing Checklist**
   - [ ] Validate with Google Rich Results Test
   - [ ] Check for JSON syntax errors
   - [ ] Verify all URLs are absolute (not relative)
   - [ ] Test with actual dealer data (not placeholders)
   - [ ] Validate hours format (ISO 8601)

---

## 🗓️ 5-Day Timeline

### **Day 1: Research**
**Morning** (9am-12pm):
- Participate in "Zero-Click Challenge" (Exercise 0)
- Analyze what data LLMs show for successful businesses

**Afternoon** (1pm-5pm):
- Study Schema.org documentation:
  - LocalBusiness
  - AutomotiveBusiness
  - Vehicle
  - Offer
- Review Google's Structured Data guidelines
- Examine real-world examples (BMW, Tesla dealers)

**Deliverable**: List of required vs. optional properties

---

### **Day 2: Template Building**
**Morning** (9am-12pm):
- Exercise 2: Schema Templates
- Create Template #1 (Metro Dealer)
- Validate with Google Rich Results Test
- Iterate until PASS

**Afternoon** (1pm-5pm):
- Create Template #2 (Rural Dealer)
- Create Template #3 (Specialty Dealer)
- Document differences between templates

**Deliverable**: 3 validated JSON-LD templates

---

### **Day 3: Advanced Templates & Testing**
**Morning** (9am-12pm):
- Create Template #4 (Dealership Group)
- Create Template #5 (EV-Focused Dealer)
- Focus on unique properties (EV charging, fleet services)

**Afternoon** (1pm-5pm):
- Comprehensive validation of all 5 templates
- Take screenshots of validation results
- Fix any warnings or errors
- Create comparison matrix

**Deliverable**: 5 validated templates + validation reports

---

### **Day 4: Integration & Documentation**
**Morning** (9am-12pm):
- Write Template Selection Guide
- Create decision tree (which template for which dealer?)
- Peer review with Squad A (data model alignment)

**Afternoon** (1pm-5pm):
- Write Implementation Guide
- Create code examples for variable substitution
- Test integration with Squad A's API (`GET /dealers/{id}/schema`)
- Verify dynamic schema generation works

**Deliverable**: Template Guide + Implementation Guide

---

### **Day 5: Polish & RFP**
**Morning** (9am-11am):
- Final review of all templates
- Create visual comparison (table/chart)
- Prepare demo

**Late Morning** (11am-12pm):
- Present templates to "FordDirect CIO"
- Explain design decisions
- Show validation results

**Afternoon** (1pm-3pm):
- Write RFP Technical Appendix
- Include all 5 templates
- Add implementation recommendations

---

## 🛠️ Technical Setup

### **Tools You'll Need**
- **VS Code** with JSON schema validation extension
- **Google Rich Results Test**: https://search.google.com/test/rich-results
- **Schema.org Validator**: https://validator.schema.org/
- **JSON-LD Playground**: https://json-ld.org/playground/

### **Browser Extension**
- Install "Schema.org Extractor" (Chrome/Firefox)
- Analyze competitor dealer sites

### **VS Code Setup**
```json
// .vscode/settings.json
{
  "json.schemas": [
    {
      "fileMatch": ["*-dealer.json"],
      "url": "https://json.schemastore.org/schema-org.json"
    }
  ],
  "json.format.enable": true
}
```

---

## 📊 Success Criteria

### **Minimum Viable** (Must Have)
- ✅ 3 templates created (Metro, Rural, Specialty)
- ✅ All templates pass Google Rich Results Test
- ✅ Basic validation documentation

### **Target** (Should Have)
- ✅ 5 templates (all dealer types)
- ✅ Template Selection Guide
- ✅ Implementation Guide for FordDirect
- ✅ Integration with Squad A's API

### **Stretch** (Nice to Have)
- ✅ Template generator tool (web form → JSON-LD)
- ✅ Diff tool (compare templates side-by-side)
- ✅ A/B testing recommendations (which properties matter most?)

---

## 🚧 Common Challenges & Solutions

### **Challenge 1: "Too many optional properties - which ones matter?"**
**Solution**: Focus on properties that answer customer questions:
- ✅ Critical: name, address, phone, hours, inventory
- ✅ Important: reviews, services, amenities
- ❌ Skip: logo, founder, parent organization (unless mega-dealer)

### **Challenge 2: "How do we handle 100+ vehicles in inventory?"**
**Solution**: Don't include all vehicles in JSON-LD. Use:
- `hasOfferCatalog` for aggregate inventory
- `makesOffer` for 1-3 featured vehicles (F-150s, hot sellers)
- Link to full inventory API

### **Challenge 3: "Hours vary by department (sales vs. service)"**
**Solution**: Use `department` property with separate `OpeningHoursSpecification`:
```json
{
  "@type": "AutoRepair",
  "name": "Service Department",
  "openingHours": "Mo-Fr 07:00-18:00"
}
```

### **Challenge 4: "How to handle temporary changes (holiday hours)?"**
**Solution**: Use `specialOpeningHoursSpecification`:
```json
{
  "@type": "OpeningHoursSpecification",
  "validFrom": "2024-12-24",
  "validThrough": "2024-12-25",
  "opens": "00:00",
  "closes": "00:00",
  "dayOfWeek": "Wednesday"
}
```

---

## 🤝 Collaboration Points

### **With Squad A (Data Architects)**
**They need from you**:
- Template structure (what properties exist?)
- Validation rules (what's required?)

**You need from them**:
- Dealer data model (what data is available?)
- API to fetch dealer details (for dynamic schema generation)

**Integration Point**: Day 4 - Your templates become Squad A's `/schema` endpoint output

### **With Squad B (Measurement Engineers)**
**They need from you**:
- Completed templates (to understand what data is exposed to LLMs)

**You need from them**:
- Audit results (which dealers appear in AI? Which don't?)
- Feedback on what properties LLMs actually use

**Integration Point**: Day 4 - Run audit before/after adding schemas (measure impact)

---

## 📖 Resources

### **Schema.org Documentation**
- [LocalBusiness](https://schema.org/LocalBusiness)
- [AutomotiveBusiness](https://schema.org/AutomotiveBusiness)
- [Vehicle](https://schema.org/Vehicle)
- [Offer](https://schema.org/Offer)
- [Product](https://schema.org/Product)

### **Google Guidelines**
- [Structured Data Guidelines](https://developers.google.com/search/docs/appearance/structured-data/sd-policies)
- [Local Business Structured Data](https://developers.google.com/search/docs/appearance/structured-data/local-business)
- [Product Structured Data](https://developers.google.com/search/docs/appearance/structured-data/product)

### **Examples**
- [BMW Dealer Schema.org](https://www.bmwofmurray.com/) (view source)
- [Tesla Store Schema.org](https://www.tesla.com/findus) (inspect element)

### **Validation Tools**
- [Google Rich Results Test](https://search.google.com/test/rich-results)
- [Schema Markup Validator](https://validator.schema.org/)
- [JSON-LD Playground](https://json-ld.org/playground/)

---

## 🎯 RFP Contribution

**Your Section**: "Technical Implementation" (Appendix)

**What You'll Write** (in RFP):

1. **Schema.org Strategy**
   - Why Schema.org matters for AI search
   - Properties we're implementing
   - How this scales to 3,200 dealers

2. **Template Library**
   - 5 dealer archetypes
   - Selection criteria
   - Customization options

3. **Validation Methodology**
   - Google Rich Results Test (all templates PASS)
   - Ongoing validation process
   - Quality assurance

4. **Implementation Roadmap**
   - Phase 1: Deploy templates to 100 pilot dealers (Month 1-2)
   - Phase 2: Rollout to all 3,200 dealers (Month 3-6)
   - Phase 3: Continuous optimization based on audit results (ongoing)

5. **Technical Appendix**
   - Include all 5 JSON-LD templates
   - Code examples
   - Integration instructions

---

## 💡 Key Insight for Your Squad

**LLMs don't "read" websites like humans. They parse structured data.**

Your templates translate human-friendly dealer information into machine-readable facts that AI can cite with confidence.

**The difference**:
- ❌ Bad: HTML paragraph "We're located in Aurora and open 8am-8pm Mon-Fri"
- ✅ Good: JSON-LD `"address": { "addressLocality": "Aurora" }` + `"opens": "08:00"`

AI search engines prefer structured data because it's:
- Unambiguous (no interpretation needed)
- Scrapable (easy to extract)
- Verifiable (can check against other sources)

**Your competitive advantage**: Understanding the difference between "human SEO" and "machine SEO."

---

## 🚀 Getting Started (Day 1 AM)

1. Read this brief fully
2. Participate in Exercise 0 (Zero-Click Challenge)
3. Meet with your squad (1 hour)
4. Assign roles:
   - **Lead**: Template architecture, RFP writing
   - **Builder**: Create JSON-LD templates
   - **Validator**: Testing, documentation
5. Set up VS Code with JSON schema validation
6. Study 3 real automotive dealer websites (view source, find JSON-LD)

**First Task**: Find 5 dealer websites with good JSON-LD markup. Analyze what they do right.

---

**Good luck, Schema Specialists! You're teaching AI how to understand dealers. 📋**
