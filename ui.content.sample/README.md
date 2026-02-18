# AEM BMAD Showcase - Sample Content Package

## Overview

This package contains sample content for demonstration and testing purposes. It includes pre-authored pages with all showcase components configured.

## Contents

```
/content/bmad-showcase/en/
├── home                    # Homepage with Hero, CardGrid, Carousel
├── about                   # About page with text and cards
├── products/               # Product listing pages
├── features/               # Feature detail pages
│   ├── ai                  # AI/LLM features
│   ├── components          # Component library
│   └── methodology         # BMAD methodology
├── docs/                   # Documentation pages
│   ├── developer           # Developer guide
│   ├── architecture        # Architecture docs
│   └── testing             # Testing strategy
└── contact                 # Contact form page

/content/dam/bmad-showcase/
├── images/                 # Sample images
│   ├── hero-bg.jpg
│   ├── ai-icon.png
│   ├── components-icon.png
│   └── methodology-icon.png
└── documents/              # Sample PDFs
```

## Installation

### Via Maven
```bash
# Install to local AEM
mvn clean install -PautoInstallSampleContent -pl ui.content.sample

# Or with full project
mvn clean install -PautoInstallPackage,autoInstallSampleContent
```

### Via Package Manager
1. Build: `mvn clean package -pl ui.content.sample`
2. Upload: Go to `/crx/packmgr/index.jsp`
3. Install: Click "Install" on the uploaded package

## Usage

### For Demo
- Access homepage: `/content/bmad-showcase/en/home.html`
- All pages use showcase components
- Content is fully editable in Author

### For Testing
- Smoke tests: Run against sample content URLs
- Component tests: Each page demonstrates component variants
- Visual regression: Use as baseline screenshots

### For Development
- Fork/modify content as needed
- Use as reference for content structure
- Test new components by adding to pages

## Customization

To create your own sample content:

1. Copy this module structure
2. Update `filter.xml` with your paths
3. Create content XML files
4. Update `pom.xml` with your project details

## Content Conventions

- **Page titles**: Clear, descriptive
- **jcr:description**: SEO-friendly descriptions
- **navTitle**: Short navigation labels
- **Images**: Use `/content/dam/bmad-showcase/`

## Notes

- This package is NOT deployed to production
- CloudManager target is set to `none`
- Safe to modify/delete without affecting production
