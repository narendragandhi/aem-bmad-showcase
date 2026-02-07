# Content Models

This document defines the content models for the AEM templates and components.

## Page Templates

### Home Page Template

- **Fields**:
    - `pageTitle` (Text)
    - `metaDescription` (Text Area)
    - `hero` (Component: Hero)
    - `body` (Container: Allows other components to be added)

### Standard Page Template

- **Fields**:
    - `pageTitle` (Text)
    - `metaDescription` (Text Area)
    - `header` (Component: Header)
    - `body` (Container: Allows other components to be added)
    - `footer` (Component: Footer)

## Components

### Hero Component

- **Fields**:
    - `heading` (Text)
    - `subheading` (Text)
    - `backgroundImage` (Image)
    - `ctaButtonText` (Text)
    - `ctaButtonLink` (Link)

### Text with Image Component

- **Fields**:
    - `heading` (Text)
    - `bodyText` (Rich Text)
    - `image` (Image)
    - `imagePosition` (Dropdown: Left, Right)
