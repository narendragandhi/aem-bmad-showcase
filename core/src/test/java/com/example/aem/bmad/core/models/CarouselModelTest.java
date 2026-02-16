package com.example.aem.bmad.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CarouselModel.
 *
 * Specification: SPEC-CAROUSEL-001
 * User Story: US-CA-007
 */
@ExtendWith(AemContextExtension.class)
class CarouselModelTest {

    private final AemContext context = new AemContext();

    private static final String COMPONENT_PATH = "/content/test/carousel";

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(CarouselModel.class, CarouselSlideModel.class);
    }

    @Nested
    @DisplayName("Autoplay Settings")
    class AutoplaySettings {

        @Test
        @DisplayName("Should return autoplay as false by default")
        void autoplayDefaultFalse() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertFalse(model.isAutoplay());
        }

        @Test
        @DisplayName("Should return autoplay as true when configured")
        void autoplayTrue() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE,
                "autoplay", true
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertTrue(model.isAutoplay());
        }

        @Test
        @DisplayName("Should return default autoplay speed when not set")
        void defaultAutoplaySpeed() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertEquals(5000L, model.getAutoplaySpeed());
        }

        @Test
        @DisplayName("Should return configured autoplay speed")
        void customAutoplaySpeed() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE,
                "autoplaySpeed", 3000L
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertEquals(3000L, model.getAutoplaySpeed());
        }
    }

    @Nested
    @DisplayName("Navigation Controls")
    class NavigationControls {

        @Test
        @DisplayName("Should show indicators by default")
        void showIndicatorsDefault() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertTrue(model.isShowIndicators());
        }

        @Test
        @DisplayName("Should hide indicators when configured")
        void hideIndicators() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE,
                "showIndicators", false
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertFalse(model.isShowIndicators());
        }

        @Test
        @DisplayName("Should show arrows by default")
        void showArrowsDefault() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertTrue(model.isShowArrows());
        }

        @Test
        @DisplayName("Should hide arrows when configured")
        void hideArrows() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE,
                "showArrows", false
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertFalse(model.isShowArrows());
        }

        @Test
        @DisplayName("Should pause on hover by default")
        void pauseOnHoverDefault() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertTrue(model.isPauseOnHover());
        }
    }

    @Nested
    @DisplayName("Slides Collection")
    class SlidesCollection {

        @Test
        @DisplayName("Should return empty list when no slides")
        void emptySlides() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertFalse(model.hasSlides());
            assertEquals(0, model.getSlideCount());
            assertTrue(model.getSlides().isEmpty());
        }

        @Test
        @DisplayName("Should return slides when configured")
        void withSlides() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE
            );
            context.create().resource(COMPONENT_PATH + "/slides/slide1",
                "image", "/content/dam/slide1.jpg",
                "heading", "Slide 1"
            );
            context.create().resource(COMPONENT_PATH + "/slides/slide2",
                "image", "/content/dam/slide2.jpg",
                "heading", "Slide 2"
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertTrue(model.hasSlides());
            assertEquals(2, model.getSlideCount());
        }
    }

    @Nested
    @DisplayName("JSON Export")
    class JsonExport {

        @Test
        @DisplayName("Should export correct resource type")
        void exportedType() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CarouselModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            CarouselModel model = context.request().adaptTo(CarouselModel.class);

            assertNotNull(model);
            assertEquals(CarouselModel.RESOURCE_TYPE, model.getExportedType());
        }
    }
}
