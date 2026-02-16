# Component Design

This document provides detailed technical designs for the AEM components, including Sling Models, dialogs, and HTL scripts.

## Component Architecture Overview

All components follow a consistent architecture pattern:

```
/apps/aem-bmad-showcase/components/
  {component-name}/
    _cq_dialog/
      .content.xml       # Component dialog definition
    _cq_editConfig.xml   # Edit configuration (optional)
    {component-name}.html # HTL template
    .content.xml         # Component node definition
```

## Core Components

### Hero Component

**Purpose**: Display a prominent hero section with background image, heading, subheading, and call-to-action button.

**Resource Type**: `aem-bmad-showcase/components/hero`

**Sling Model**: `com.example.aem.bmad.core.models.HeroModel`

```java
@Model(adaptables = Resource.class, 
       adapters = HeroModel.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroModel {

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String subheading;

    @ValueMapValue
    private String backgroundImage;

    @ValueMapValue
    private String ctaButtonText;

    @ValueMapValue
    private String ctaButtonLink;

    @ValueMapValue
    private String ctaButtonTarget;

    @ValueMapValue
    private String overlayOpacity;

    // Getters
    public String getHeading() { return heading; }
    public String getSubheading() { return subheading; }
    public String getBackgroundImage() { return backgroundImage; }
    public String getCtaButtonText() { return ctaButtonText; }
    public String getCtaButtonLink() { return ctaButtonLink; }
    public String getCtaButtonTarget() { return ctaButtonTarget; }
    public String getOverlayOpacity() { return overlayOpacity; }

    public boolean hasCta() {
        return StringUtils.isNotBlank(ctaButtonText) && 
               StringUtils.isNotBlank(ctaButtonLink);
    }
}
```

**Dialog Fields**:
| Field | Type | Tab | Required |
|-------|------|-----|----------|
| heading | textfield | Content | Yes |
| subheading | textfield | Content | No |
| backgroundImage | pathfield (image) | Content | Yes |
| ctaButtonText | textfield | CTA | No |
| ctaButtonLink | pathfield | CTA | No |
| ctaButtonTarget | select (_self, _blank) | CTA | No |
| overlayOpacity | select (0, 25, 50, 75) | Styling | No |

**HTL Template** (`hero.html`):
```html
<sly data-sly-use.model="com.example.aem.bmad.core.models.HeroModel"/>
<section class="hero" 
         data-sly-test="${model.heading}"
         style="background-image: url('${model.backgroundImage}');"
         aria-label="${'Hero section' @ i18n}">
    <div class="hero__overlay" style="opacity: ${model.overlayOpacity || '50'}%;"></div>
    <div class="hero__content">
        <h1 class="hero__heading">${model.heading}</h1>
        <p data-sly-test="${model.subheading}" class="hero__subheading">
            ${model.subheading}
        </p>
        <a data-sly-test="${model.hasCta}" 
           href="${model.ctaButtonLink}" 
           target="${model.ctaButtonTarget || '_self'}"
           class="hero__cta btn btn--primary">
            ${model.ctaButtonText}
        </a>
    </div>
</section>
```

---

### Text with Image Component

**Purpose**: Display a content block with text alongside an image, with configurable layout options.

**Resource Type**: `aem-bmad-showcase/components/textwithimage`

**Sling Model**: `com.example.aem.bmad.core.models.TextWithImageModel`

```java
@Model(adaptables = Resource.class, 
       adapters = TextWithImageModel.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextWithImageModel {

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String bodyText;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String imageAlt;

    @ValueMapValue
    private String imagePosition; // "left" or "right"

    @ValueMapValue
    private String verticalAlignment; // "top", "center", "bottom"

    // Getters
    public String getHeading() { return heading; }
    public String getBodyText() { return bodyText; }
    public String getImage() { return image; }
    public String getImageAlt() { return imageAlt; }
    public String getImagePosition() { 
        return StringUtils.defaultIfBlank(imagePosition, "left"); 
    }
    public String getVerticalAlignment() { 
        return StringUtils.defaultIfBlank(verticalAlignment, "center"); 
    }

    public String getLayoutClass() {
        return "textwithimage--" + getImagePosition() + 
               " textwithimage--align-" + getVerticalAlignment();
    }
}
```

**Dialog Fields**:
| Field | Type | Tab | Required |
|-------|------|-----|----------|
| heading | textfield | Content | No |
| bodyText | richtext | Content | Yes |
| image | pathfield (image) | Content | Yes |
| imageAlt | textfield | Content | Yes |
| imagePosition | select (left, right) | Layout | No |
| verticalAlignment | select (top, center, bottom) | Layout | No |

---

### Carousel Component

**Purpose**: Display multiple slides in a rotating carousel with navigation controls.

**Resource Type**: `aem-bmad-showcase/components/carousel`

**Sling Model**: `com.example.aem.bmad.core.models.CarouselModel`

```java
@Model(adaptables = Resource.class, 
       adapters = CarouselModel.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselModel {

    @ChildResource
    private List<CarouselSlide> slides;

    @ValueMapValue
    private boolean autoplay;

    @ValueMapValue
    private int autoplayInterval; // in milliseconds

    @ValueMapValue
    private boolean showNavigation;

    @ValueMapValue
    private boolean showPagination;

    // Getters
    public List<CarouselSlide> getSlides() { return slides; }
    public boolean isAutoplay() { return autoplay; }
    public int getAutoplayInterval() { 
        return autoplayInterval > 0 ? autoplayInterval : 5000; 
    }
    public boolean isShowNavigation() { return showNavigation; }
    public boolean isShowPagination() { return showPagination; }

    public boolean hasSlides() {
        return slides != null && !slides.isEmpty();
    }
}
```

**Nested Model - CarouselSlide**:
```java
@Model(adaptables = Resource.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselSlide {

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String imageAlt;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String linkUrl;

    @ValueMapValue
    private String linkText;

    // Getters
}
```

---

### Card Grid Component

**Purpose**: Display a grid of cards for products, services, or content items.

**Resource Type**: `aem-bmad-showcase/components/cardgrid`

**Sling Model**: `com.example.aem.bmad.core.models.CardGridModel`

```java
@Model(adaptables = Resource.class, 
       adapters = CardGridModel.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardGridModel {

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String subheading;

    @ChildResource
    private List<Card> cards;

    @ValueMapValue
    private int columnsDesktop; // 2, 3, or 4

    @ValueMapValue
    private int columnsTablet; // 1, 2, or 3

    @ValueMapValue
    private int columnsMobile; // 1 or 2

    // Getters
    public String getHeading() { return heading; }
    public String getSubheading() { return subheading; }
    public List<Card> getCards() { return cards; }
    public int getColumnsDesktop() { 
        return columnsDesktop > 0 ? columnsDesktop : 3; 
    }
    public int getColumnsTablet() { 
        return columnsTablet > 0 ? columnsTablet : 2; 
    }
    public int getColumnsMobile() { 
        return columnsMobile > 0 ? columnsMobile : 1; 
    }

    public String getGridClass() {
        return String.format("cardgrid--desktop-%d cardgrid--tablet-%d cardgrid--mobile-%d",
            getColumnsDesktop(), getColumnsTablet(), getColumnsMobile());
    }
}
```

---

### Language Switcher Component

**Purpose**: Allow users to switch between different language versions of the current page.

**Resource Type**: `aem-bmad-showcase/components/languageswitcher`

**Sling Model**: `com.example.aem.bmad.core.models.LanguageSwitcherModel`

```java
@Model(adaptables = SlingHttpServletRequest.class, 
       adapters = LanguageSwitcherModel.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LanguageSwitcherModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ScriptVariable
    private Page currentPage;

    @OSGiService
    private LanguageManager languageManager;

    private List<LanguageOption> languageOptions;

    @PostConstruct
    protected void init() {
        languageOptions = new ArrayList<>();
        Page languageRoot = languageManager.getLanguageRoot(currentPage);
        
        if (languageRoot != null) {
            Iterator<Page> languagePages = languageRoot.getParent().listChildren();
            while (languagePages.hasNext()) {
                Page langPage = languagePages.next();
                String languageCode = langPage.getLanguage(false).getLanguage();
                String languageName = langPage.getLanguage(false).getDisplayName();
                String correspondingPagePath = getCorrespondingPath(langPage, currentPage);
                boolean isCurrent = langPage.equals(languageRoot);
                
                languageOptions.add(new LanguageOption(
                    languageCode, languageName, correspondingPagePath, isCurrent));
            }
        }
    }

    public List<LanguageOption> getLanguageOptions() {
        return languageOptions;
    }
}
```

---

## Component Best Practices

### Accessibility

1. All interactive elements must have appropriate ARIA attributes
2. Images must have meaningful alt text (managed through dialog)
3. Color contrast must meet WCAG 2.1 AA standards
4. Focus states must be clearly visible
5. Keyboard navigation must be fully supported

### Internationalization

1. All static text must use i18n dictionaries
2. Support for RTL languages via CSS logical properties
3. Date and number formatting must respect locale settings

### Performance

1. Images should use lazy loading for below-the-fold content
2. Client libraries should be minified and combined
3. Critical CSS should be inlined for above-the-fold components

### Testing Requirements

Each component must have:
- Unit tests for Sling Models (minimum 80% coverage)
- Dialog validation tests
- Accessibility tests using automated tools
- Visual regression tests for key states
