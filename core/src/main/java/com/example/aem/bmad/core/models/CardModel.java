package com.example.aem.bmad.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for the Card component.
 *
 * Specification: SPEC-CARD-001
 * User Story: US-CA-008
 *
 * @see <a href="../bmad/03-Architecture-Design/component-design.md#card-component">Component Design</a>
 */
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {CardModel.class, ComponentExporter.class},
    resourceType = CardModel.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CardModel implements ComponentExporter {

    static final String RESOURCE_TYPE = "aem-bmad-showcase/components/content/card";

    public enum Variant {
        DEFAULT("default"),
        OUTLINED("outlined"),
        ELEVATED("elevated");

        private final String value;

        Variant(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String imageAlt;

    @ValueMapValue
    private String eyebrow;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String ctaText;

    @ValueMapValue
    private String ctaLink;

    @ValueMapValue
    private String variant;

    public String getImage() {
        return image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public String getEyebrow() {
        return eyebrow;
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

    public String getVariant() {
        return variant != null ? variant : Variant.DEFAULT.getValue();
    }

    public String getVariantClass() {
        return "cmp-card--" + getVariant();
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

    public boolean hasEyebrow() {
        return eyebrow != null && !eyebrow.isEmpty();
    }

    public boolean hasDescription() {
        return description != null && !description.isEmpty();
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
