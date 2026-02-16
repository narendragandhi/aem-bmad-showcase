package com.example.aem.bmad.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for individual Carousel slides.
 *
 * Part of SPEC-CAROUSEL-001
 */
@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CarouselSlideModel {

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String imageAlt;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String ctaText;

    @ValueMapValue
    private String ctaLink;

    public String getImage() {
        return image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getCtaText() {
        return ctaText;
    }

    public String getCtaLink() {
        return ctaLink;
    }

    public boolean hasImage() {
        return image != null && !image.isEmpty();
    }

    public boolean hasCta() {
        return ctaText != null && !ctaText.isEmpty() && ctaLink != null && !ctaLink.isEmpty();
    }

    public boolean isExternalLink() {
        return ctaLink != null && ctaLink.startsWith("http");
    }
}
