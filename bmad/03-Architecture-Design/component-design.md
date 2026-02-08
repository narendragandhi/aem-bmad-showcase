# Component Design

This document contains the detailed design for the AEM components, with a strong emphasis on integrating with the established design system. All components must leverage design tokens, adhere to specified UI patterns, and ensure accessibility from the ground up.

## General Component Design Principles

- **Design System Adherence**: All components must strictly follow the guidelines, use the design tokens (colors, typography, spacing), and conform to the patterns defined in the [Design System Integration](../02-Model-Definition/design-system.md) document.
- **Accessibility First**: Components will be designed and implemented to meet WCAG 2.1 AA standards, including keyboard navigation, proper ARIA attributes, and semantic HTML.
- **Internationalization (i18n) Ready**: All text strings in component dialogs and rendering will be externalized and managed through AEM's internationalization features to support multi-lingual content.

## Hero Component

- **Sling Model**: `com.example.aem.bmad.core.models.HeroModel`
    - Injects the JCR properties from the dialog.
    - Exposes the heading, subheading, image path, and CTA link to the HTL script.
    - Ensures all text fields are i18n-ready.
- **Dialog**:
    - `heading`: Text field (i18n-enabled)
    - `subheading`: Text field (i18n-enabled)
    - `backgroundImage`: Path field (for selecting an image from the DAM)
    - `ctaButtonText`: Text field (i18n-enabled)
    - `ctaButtonLink`: Path field (for selecting a page or entering an external URL)
- **HTL Script**: (`hero.html`)
    - Renders the component's markup using the data from the Sling Model.
    - Uses the `data-sly-use` attribute to instantiate the Sling Model.
    - Applies design system classes for styling and layout.
    - Includes necessary ARIA attributes for accessibility.

## Text with Image Component

- **Sling Model**: `com.example.aem.bmad.core.models.TextWithImageModel`
    - Injects the JCR properties from the dialog.
    - Exposes the heading, body text, image path, and image position to the HTL script.
    - Ensures all text fields are i18n-ready.
- **Dialog**:
    - `heading`: Text field (i18n-enabled)
    - `bodyText`: Rich Text Editor (i18n-enabled)
    - `image`: Path field
    - `imagePosition`: Select field (options: Left, Right)
- **HTL Script**: (`textwithimage.html`)
    - Renders the component's markup.
    - Applies a CSS class based on the `imagePosition` property to control the layout, leveraging design system spacing tokens.
    - Includes necessary ARIA attributes for accessibility.
