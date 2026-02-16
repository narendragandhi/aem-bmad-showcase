# Content Models

This document defines the content models for the AEM templates and components.

## Page Templates

### Home Page Template

**Template Path**: `/conf/aem-bmad-showcase/settings/wcm/templates/home-page`

- **Fields**:
    - `pageTitle` (Text) - Required
    - `metaDescription` (Text Area) - Required, max 160 characters
    - `canonicalUrl` (Link) - Optional, for SEO
    - `ogImage` (Image) - Open Graph image for social sharing
- **Structure**:
    - `hero` (Component: Hero) - Locked, required
    - `body` (Container: Responsive Grid) - Allows layout components
    - `footer` (Component: Footer) - Locked, inherited from template

### Standard Page Template

**Template Path**: `/conf/aem-bmad-showcase/settings/wcm/templates/standard-page`

- **Fields**:
    - `pageTitle` (Text) - Required
    - `metaDescription` (Text Area) - Required
    - `hideInNavigation` (Checkbox) - Exclude from navigation
    - `redirectTarget` (Link) - For redirect pages
- **Structure**:
    - `header` (Component: Header) - Locked, inherited
    - `breadcrumb` (Component: Breadcrumb) - Locked
    - `body` (Container: Responsive Grid) - Allows all content components
    - `sidebar` (Container) - Optional, for secondary content
    - `footer` (Component: Footer) - Locked, inherited

### Product Page Template

**Template Path**: `/conf/aem-bmad-showcase/settings/wcm/templates/product-page`

- **Fields**:
    - `pageTitle` (Text) - Required
    - `productId` (Text) - Required, unique identifier
    - `productCategory` (Tags) - Product categorization
    - `featuredImage` (Image) - Main product image
    - `shortDescription` (Text Area) - Summary for listings
- **Structure**:
    - `header` (Component: Header) - Locked
    - `productHero` (Component: Product Hero) - Locked
    - `productDetails` (Container) - For specifications
    - `relatedProducts` (Component: Card Grid) - Configurable
    - `footer` (Component: Footer) - Locked

### Landing Page Template

**Template Path**: `/conf/aem-bmad-showcase/settings/wcm/templates/landing-page`

- **Fields**:
    - `pageTitle` (Text) - Required
    - `campaignId` (Text) - For analytics tracking
    - `hideNavigation` (Checkbox) - For focused landing pages
- **Structure**:
    - `hero` (Component: Hero) - Editable
    - `body` (Container: Responsive Grid) - Full-width sections
    - `conversionForm` (Component: Form Container) - Optional

## Components

### Hero Component

**Resource Type**: `aem-bmad-showcase/components/hero`

- **Fields**:
    - `heading` (Text) - Required, max 80 characters
    - `subheading` (Text) - Optional, max 150 characters
    - `backgroundImage` (Image) - Required, recommended 1920x800px
    - `backgroundImageAlt` (Text) - Required for accessibility
    - `ctaButtonText` (Text) - Optional
    - `ctaButtonLink` (Link) - Required if CTA text is set
    - `ctaButtonTarget` (Select: _self, _blank) - Default: _self
    - `overlayOpacity` (Select: 0%, 25%, 50%, 75%) - Default: 50%
    - `textAlignment` (Select: left, center, right) - Default: center

### Text with Image Component

**Resource Type**: `aem-bmad-showcase/components/textwithimage`

- **Fields**:
    - `heading` (Text) - Optional
    - `bodyText` (Rich Text) - Required
    - `image` (Image) - Required
    - `imageAlt` (Text) - Required for accessibility
    - `imagePosition` (Select: left, right) - Default: left
    - `verticalAlignment` (Select: top, center, bottom) - Default: center
    - `imageSizeRatio` (Select: 40%, 50%, 60%) - Default: 50%

### Carousel Component

**Resource Type**: `aem-bmad-showcase/components/carousel`

- **Fields**:
    - `autoplay` (Checkbox) - Default: false
    - `autoplayInterval` (Number) - Default: 5000ms
    - `showNavigation` (Checkbox) - Show prev/next arrows
    - `showPagination` (Checkbox) - Show dot indicators
    - `pauseOnHover` (Checkbox) - Pause autoplay on hover
- **Child Items (Slides)**:
    - `image` (Image) - Required
    - `imageAlt` (Text) - Required
    - `heading` (Text) - Optional
    - `description` (Text Area) - Optional
    - `linkUrl` (Link) - Optional
    - `linkText` (Text) - Required if link URL is set

### Card Grid Component

**Resource Type**: `aem-bmad-showcase/components/cardgrid`

- **Fields**:
    - `heading` (Text) - Optional section heading
    - `subheading` (Text) - Optional section subheading
    - `columnsDesktop` (Select: 2, 3, 4) - Default: 3
    - `columnsTablet` (Select: 1, 2, 3) - Default: 2
    - `columnsMobile` (Select: 1, 2) - Default: 1
    - `cardStyle` (Select: standard, bordered, elevated) - Default: standard
- **Child Items (Cards)**:
    - `image` (Image) - Optional
    - `imageAlt` (Text) - Required if image is set
    - `heading` (Text) - Required
    - `description` (Text Area) - Optional
    - `linkUrl` (Link) - Optional
    - `linkText` (Text) - Default: "Learn More"
    - `tags` (Tags) - Optional categorization

### Accordion Component

**Resource Type**: `aem-bmad-showcase/components/accordion`

- **Fields**:
    - `heading` (Text) - Optional section heading
    - `expandFirst` (Checkbox) - Expand first item by default
    - `allowMultiple` (Checkbox) - Allow multiple items open
- **Child Items (Accordion Items)**:
    - `title` (Text) - Required
    - `content` (Rich Text) - Required
    - `icon` (Icon Picker) - Optional

### Tabs Component

**Resource Type**: `aem-bmad-showcase/components/tabs`

- **Fields**:
    - `tabStyle` (Select: horizontal, vertical) - Default: horizontal
    - `tabAlignment` (Select: left, center, stretch) - Default: left
- **Child Items (Tab Panels)**:
    - `tabTitle` (Text) - Required
    - `tabIcon` (Icon Picker) - Optional
    - `content` (Container) - Allows nested components

### Language Switcher Component

**Resource Type**: `aem-bmad-showcase/components/languageswitcher`

- **Fields**:
    - `displayStyle` (Select: dropdown, inline, flags) - Default: dropdown
    - `showCurrentLanguage` (Checkbox) - Show current language in trigger
    - `showLanguageCode` (Checkbox) - Show language codes (e.g., EN, FR)

### Contact Form Component

**Resource Type**: `aem-bmad-showcase/components/contactform`

- **Fields**:
    - `heading` (Text) - Optional
    - `successMessage` (Rich Text) - Required
    - `errorMessage` (Text) - Required
    - `submitButtonText` (Text) - Default: "Submit"
    - `crmIntegration` (Checkbox) - Enable CRM sync
- **Form Fields (Configurable)**:
    - Name fields (First, Last)
    - Email (with validation)
    - Phone (optional)
    - Subject (dropdown or text)
    - Message (text area)
    - Consent checkbox

### Breadcrumb Component

**Resource Type**: `aem-bmad-showcase/components/breadcrumb`

- **Fields**:
    - `showCurrentPage` (Checkbox) - Include current page
    - `startLevel` (Number) - Starting hierarchy level
    - `hideOnHomePage` (Checkbox) - Hide when on home page
    - `separator` (Select: /, >, |, custom) - Default: /

## Experience Fragments

### Header Experience Fragment

**Path**: `/content/experience-fragments/aem-bmad-showcase/header`

- **Components**:
    - Logo (linked to home page)
    - Main Navigation (auto-generated from site structure)
    - Language Switcher
    - Search (optional)
    - Utility Navigation (login, cart, etc.)

### Footer Experience Fragment

**Path**: `/content/experience-fragments/aem-bmad-showcase/footer`

- **Components**:
    - Footer Navigation (multi-column)
    - Social Media Links
    - Newsletter Signup (optional)
    - Copyright Text
    - Legal Links (Privacy, Terms, etc.)
    - Language Switcher (secondary)
