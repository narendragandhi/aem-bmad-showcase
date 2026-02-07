# Component Design

This document contains the detailed design for the AEM components.

## Hero Component

- **Sling Model**: `com.example.aem.bmad.core.models.HeroModel`
    - Injects the JCR properties from the dialog.
    - Exposes the heading, subheading, image path, and CTA link to the HTL script.
- **Dialog**:
    - `heading`: Text field
    - `subheading`: Text field
    - `backgroundImage`: Path field (for selecting an image from the DAM)
    - `ctaButtonText`: Text field
    - `ctaButtonLink`: Path field (for selecting a page or entering an external URL)
- **HTL Script**: (`hero.html`)
    - Renders the component's markup using the data from the Sling Model.
    - Uses the `data-sly-use` attribute to instantiate the Sling Model.

## Text with Image Component

- **Sling Model**: `com.example.aem.bmad.core.models.TextWithImageModel`
    - Injects the JCR properties from the dialog.
    - Exposes the heading, body text, image path, and image position to the HTL script.
- **Dialog**:
    - `heading`: Text field
    - `bodyText`: Rich Text Editor
    - `image`: Path field
    - `imagePosition`: Select field (options: Left, Right)
- **HTL Script**: (`textwithimage.html`)
    - Renders the component's markup.
    - Applies a CSS class based on the `imagePosition` property to control the layout.
