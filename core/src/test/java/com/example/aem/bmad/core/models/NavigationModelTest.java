package com.example.aem.bmad.core.models;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NavigationModel.
 *
 * Specification: SPEC-NAV-001
 * User Story: US-CA-010
 */
@ExtendWith(AemContextExtension.class)
class NavigationModelTest {

    private final AemContext context = new AemContext();

    private static final String COMPONENT_PATH = "/content/test/page/jcr:content/navigation";
    private static final String ROOT_PATH = "/content/test";

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(NavigationModel.class);

        // Create page structure
        Page rootPage = context.create().page(ROOT_PATH, "template", "Site Root");
        Page page1 = context.create().page(ROOT_PATH + "/page1", "template", "Page 1");
        Page page2 = context.create().page(ROOT_PATH + "/page2", "template", "Page 2");
        context.create().page(ROOT_PATH + "/page1/child1", "template", "Child 1");
        context.create().page(ROOT_PATH + "/page1/child2", "template", "Child 2");

        // Set current page
        context.currentPage(ROOT_PATH + "/page1");
    }

    @Nested
    @DisplayName("Navigation Configuration")
    class NavigationConfiguration {

        @Test
        @DisplayName("Should return empty items when no navigation root")
        void emptyWithoutRoot() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertFalse(model.hasItems());
            assertTrue(model.getItems().isEmpty());
        }

        @Test
        @DisplayName("Should return navigation root path")
        void navigationRootPath() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE,
                "navigationRoot", ROOT_PATH
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertEquals(ROOT_PATH, model.getNavigationRoot());
        }

        @Test
        @DisplayName("Should return default structure depth")
        void defaultStructureDepth() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE,
                "navigationRoot", ROOT_PATH
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertEquals(2, model.getStructureDepth());
        }

        @Test
        @DisplayName("Should return configured structure depth")
        void customStructureDepth() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE,
                "navigationRoot", ROOT_PATH,
                "structureDepth", 3
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertEquals(3, model.getStructureDepth());
        }
    }

    @Nested
    @DisplayName("Navigation Tree Building")
    class NavigationTree {

        @Test
        @DisplayName("Should include root page when not skipped")
        void includeRootPage() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE,
                "navigationRoot", ROOT_PATH,
                "skipNavigationRoot", false
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertTrue(model.hasItems());
            assertEquals(1, model.getItems().size());
            assertEquals("Site Root", model.getItems().get(0).getTitle());
        }

        @Test
        @DisplayName("Should skip root page when configured")
        void skipRootPage() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE,
                "navigationRoot", ROOT_PATH,
                "skipNavigationRoot", true
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertTrue(model.hasItems());
            // Should have page1 and page2 as top level items
            assertTrue(model.getItems().size() >= 2);
        }

        @Test
        @DisplayName("Should build navigation items with correct URLs")
        void navigationItemUrls() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE,
                "navigationRoot", ROOT_PATH,
                "skipNavigationRoot", true
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertTrue(model.hasItems());

            NavigationModel.NavigationItem firstItem = model.getItems().get(0);
            assertTrue(firstItem.getUrl().endsWith(".html"));
        }
    }

    @Nested
    @DisplayName("Active/Current State")
    class ActiveState {

        @Test
        @DisplayName("Should track current path")
        void currentPath() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE,
                "navigationRoot", ROOT_PATH
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertEquals(ROOT_PATH + "/page1", model.getCurrentPath());
        }
    }

    @Nested
    @DisplayName("Navigation Items")
    class NavigationItems {

        @Test
        @DisplayName("Navigation item should have all required properties")
        void itemProperties() {
            NavigationModel.NavigationItem item = new NavigationModel.NavigationItem(
                "/content/test/page",
                "/content/test/page.html",
                "Test Page",
                true,
                false,
                0,
                java.util.Collections.emptyList()
            );

            assertEquals("/content/test/page", item.getPath());
            assertEquals("/content/test/page.html", item.getUrl());
            assertEquals("Test Page", item.getTitle());
            assertTrue(item.isActive());
            assertFalse(item.isCurrent());
            assertEquals(0, item.getLevel());
            assertFalse(item.hasChildren());
        }

        @Test
        @DisplayName("Navigation item should report children correctly")
        void itemWithChildren() {
            NavigationModel.NavigationItem child = new NavigationModel.NavigationItem(
                "/content/child",
                "/content/child.html",
                "Child",
                false,
                false,
                1,
                java.util.Collections.emptyList()
            );

            NavigationModel.NavigationItem parent = new NavigationModel.NavigationItem(
                "/content/parent",
                "/content/parent.html",
                "Parent",
                true,
                false,
                0,
                java.util.Collections.singletonList(child)
            );

            assertTrue(parent.hasChildren());
            assertEquals(1, parent.getChildren().size());
        }
    }

    @Nested
    @DisplayName("JSON Export")
    class JsonExport {

        @Test
        @DisplayName("Should export correct resource type")
        void exportedType() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", NavigationModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            NavigationModel model = context.request().adaptTo(NavigationModel.class);

            assertNotNull(model);
            assertEquals(NavigationModel.RESOURCE_TYPE, model.getExportedType());
        }
    }
}
