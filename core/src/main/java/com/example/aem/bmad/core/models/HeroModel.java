package com.example.aem.bmad.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Title;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {HeroModel.class, ComponentExporter.class},
    resourceType = HeroModel.RESOURCE_TYPE
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class HeroModel implements ComponentExporter {

    static final String RESOURCE_TYPE = "aem-bmad-showcase/components/content/hero";

    @ValueMapValue @Optional
    private String heading;

    @ValueMapValue @Optional
    private String subheading;

    @ValueMapValue @Optional
    private String backgroundImage;

    @ValueMapValue @Optional
    private String ctaButtonText;

    @ValueMapValue @Optional
    private String ctaButtonLink;

    public String getHeading() {
        return heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getCtaButtonText() {
        return ctaButtonText;
    }

    public String getCtaButtonLink() {
        return ctaButtonLink;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
