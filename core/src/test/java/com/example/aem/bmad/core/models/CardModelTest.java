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
 * Unit tests for CardModel.
 *
 * Specification: SPEC-CARD-001
 * User Story: US-CA-008
 */
@ExtendWith(AemContextExtension.class)
class CardModelTest {

    private final AemContext context = new AemContext();

    private static final String COMPONENT_PATH = "/content/test/card";

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(CardModel.class);
    }

    @Nested
    @DisplayName("Basic Card Properties")
    class BasicProperties {

        @Test
        @DisplayName("Should return all configured properties")
        void shouldReturnAllProperties() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "image", "/content/dam/test/image.jpg",
                "imageAlt", "Test image alt text",
                "eyebrow", "Featured",
                "heading", "Card Heading",
                "description", "This is a description",
                "ctaText", "Learn More",
                "ctaLink", "/content/test/page",
                "variant", "elevated"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertEquals("/content/dam/test/image.jpg", model.getImage());
            assertEquals("Test image alt text", model.getImageAlt());
            assertEquals("Featured", model.getEyebrow());
            assertEquals("Card Heading", model.getHeading());
            assertEquals("This is a description", model.getDescription());
            assertEquals("Learn More", model.getCtaText());
            assertEquals("/content/test/page", model.getCtaLink());
            assertEquals("elevated", model.getVariant());
        }

        @Test
        @DisplayName("Should return default variant when not set")
        void shouldReturnDefaultVariant() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "heading", "Card Heading"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertEquals("default", model.getVariant());
            assertEquals("cmp-card--default", model.getVariantClass());
        }
    }

    @Nested
    @DisplayName("Card Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("hasImage returns true when image is set")
        void hasImageReturnsTrue() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "image", "/content/dam/test/image.jpg"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertTrue(model.hasImage());
        }

        @Test
        @DisplayName("hasImage returns false when image is empty")
        void hasImageReturnsFalse() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "heading", "Card Heading"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertFalse(model.hasImage());
        }

        @Test
        @DisplayName("hasCta returns true when both text and link are set")
        void hasCtaReturnsTrue() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "ctaText", "Click here",
                "ctaLink", "/content/page"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertTrue(model.hasCta());
        }

        @Test
        @DisplayName("hasCta returns false when link is missing")
        void hasCtaReturnsFalseWithoutLink() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "ctaText", "Click here"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertFalse(model.hasCta());
        }

        @Test
        @DisplayName("isExternalLink returns true for http links")
        void isExternalLinkReturnsTrue() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "ctaLink", "https://example.com"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertTrue(model.isExternalLink());
        }

        @Test
        @DisplayName("isExternalLink returns false for internal links")
        void isExternalLinkReturnsFalse() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "ctaLink", "/content/page"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertFalse(model.isExternalLink());
        }

        @Test
        @DisplayName("hasEyebrow returns correctly")
        void hasEyebrowWorks() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "eyebrow", "Category"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertTrue(model.hasEyebrow());
        }

        @Test
        @DisplayName("hasDescription returns correctly")
        void hasDescriptionWorks() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "description", "Some text"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertTrue(model.hasDescription());
        }
    }

    @Nested
    @DisplayName("Variant CSS Classes")
    class VariantClasses {

        @Test
        @DisplayName("Should return correct class for elevated variant")
        void elevatedVariantClass() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "variant", "elevated"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertEquals("cmp-card--elevated", model.getVariantClass());
        }

        @Test
        @DisplayName("Should return correct class for outlined variant")
        void outlinedVariantClass() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE,
                "variant", "outlined"
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertEquals("cmp-card--outlined", model.getVariantClass());
        }
    }

    @Nested
    @DisplayName("JSON Export")
    class JsonExport {

        @Test
        @DisplayName("Should export correct resource type")
        void shouldExportResourceType() {
            context.create().resource(COMPONENT_PATH,
                "sling:resourceType", CardModel.RESOURCE_TYPE
            );
            context.currentResource(COMPONENT_PATH);

            CardModel model = context.request().adaptTo(CardModel.class);

            assertNotNull(model);
            assertEquals(CardModel.RESOURCE_TYPE, model.getExportedType());
        }
    }
}
