package com.example.aem.bmad.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for the Card Grid layout component.
 *
 * Specification: SPEC-CARDGRID-001
 * User Story: US-CA-009
 *
 * @see <a href="../bmad/03-Architecture-Design/component-design.md#card-grid-component">Component Design</a>
 */
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {CardGridModel.class, ComponentExporter.class},
    resourceType = CardGridModel.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CardGridModel implements ComponentExporter {

    static final String RESOURCE_TYPE = "aem-bmad-showcase/components/content/cardgrid";

    private static final String DEFAULT_COLUMNS = "3";
    private static final String DEFAULT_GAP = "medium";
    private static final String DEFAULT_HEADING_LEVEL = "h2";

    @ValueMapValue
    private String columns;

    @ValueMapValue
    private String gap;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String headingLevel;

    public String getColumns() {
        return columns != null ? columns : DEFAULT_COLUMNS;
    }

    public String getGap() {
        return gap != null ? gap : DEFAULT_GAP;
    }

    public String getHeading() {
        return heading;
    }

    public String getHeadingLevel() {
        return headingLevel != null ? headingLevel : DEFAULT_HEADING_LEVEL;
    }

    public boolean hasHeading() {
        return heading != null && !heading.isEmpty();
    }

    public String getGridClass() {
        return "cmp-cardgrid--cols-" + getColumns();
    }

    public String getGapClass() {
        return "cmp-cardgrid--gap-" + getGap();
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
