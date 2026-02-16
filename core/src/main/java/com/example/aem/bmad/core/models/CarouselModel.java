package com.example.aem.bmad.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Collections;
import java.util.List;

/**
 * Sling Model for the Carousel component.
 *
 * Specification: SPEC-CAROUSEL-001
 * User Story: US-CA-007
 *
 * @see <a href="../bmad/03-Architecture-Design/component-design.md#carousel-component">Component Design</a>
 */
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {CarouselModel.class, ComponentExporter.class},
    resourceType = CarouselModel.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CarouselModel implements ComponentExporter {

    static final String RESOURCE_TYPE = "aem-bmad-showcase/components/content/carousel";

    private static final long DEFAULT_AUTOPLAY_SPEED = 5000L;

    @ValueMapValue
    private Boolean autoplay;

    @ValueMapValue
    private Long autoplaySpeed;

    @ValueMapValue
    private Boolean showIndicators;

    @ValueMapValue
    private Boolean showArrows;

    @ValueMapValue
    private Boolean pauseOnHover;

    @ChildResource
    private List<CarouselSlideModel> slides;

    public boolean isAutoplay() {
        return autoplay != null && autoplay;
    }

    public long getAutoplaySpeed() {
        return autoplaySpeed != null ? autoplaySpeed : DEFAULT_AUTOPLAY_SPEED;
    }

    public boolean isShowIndicators() {
        return showIndicators == null || showIndicators;
    }

    public boolean isShowArrows() {
        return showArrows == null || showArrows;
    }

    public boolean isPauseOnHover() {
        return pauseOnHover == null || pauseOnHover;
    }

    public List<CarouselSlideModel> getSlides() {
        return slides != null ? slides : Collections.emptyList();
    }

    public boolean hasSlides() {
        return slides != null && !slides.isEmpty();
    }

    public int getSlideCount() {
        return slides != null ? slides.size() : 0;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
