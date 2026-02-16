package com.example.aem.bmad.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link HeroModel}.
 *
 * These tests validate the Sling Model behavior as specified in:
 * - bmad/03-Architecture-Design/component-design.md
 * - US-CA-003: Add Hero Component
 *
 * @see HeroModel
 */
@ExtendWith(AemContextExtension.class)
class HeroModelTest {

    private final AemContext context = new AemContext();

    private static final String RESOURCE_PATH = "/content/test/hero";
    private static final String TEST_HEADING = "Welcome to Our Site";
    private static final String TEST_SUBHEADING = "Discover amazing products";
    private static final String TEST_BACKGROUND_IMAGE = "/content/dam/images/hero-bg.jpg";
    private static final String TEST_CTA_TEXT = "Learn More";
    private static final String TEST_CTA_LINK = "/content/products";

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeroModel.class);
    }

    @Nested
    @DisplayName("Given a fully configured Hero component")
    class FullyConfiguredHero {

        private HeroModel model;

        @BeforeEach
        void setUp() {
            context.create().resource(RESOURCE_PATH,
                "sling:resourceType", HeroModel.RESOURCE_TYPE,
                "heading", TEST_HEADING,
                "subheading", TEST_SUBHEADING,
                "backgroundImage", TEST_BACKGROUND_IMAGE,
                "ctaButtonText", TEST_CTA_TEXT,
                "ctaButtonLink", TEST_CTA_LINK
            );

            Resource resource = context.resourceResolver().getResource(RESOURCE_PATH);
            context.request().setResource(resource);
            model = context.request().adaptTo(HeroModel.class);
        }

        @Test
        @DisplayName("Then model should be instantiated")
        void shouldInstantiateModel() {
            assertNotNull(model, "HeroModel should be instantiated from request");
        }

        @Test
        @DisplayName("Then heading should be returned correctly")
        void shouldReturnHeading() {
            assertEquals(TEST_HEADING, model.getHeading());
        }

        @Test
        @DisplayName("Then subheading should be returned correctly")
        void shouldReturnSubheading() {
            assertEquals(TEST_SUBHEADING, model.getSubheading());
        }

        @Test
        @DisplayName("Then background image path should be returned correctly")
        void shouldReturnBackgroundImage() {
            assertEquals(TEST_BACKGROUND_IMAGE, model.getBackgroundImage());
        }

        @Test
        @DisplayName("Then CTA button text should be returned correctly")
        void shouldReturnCtaButtonText() {
            assertEquals(TEST_CTA_TEXT, model.getCtaButtonText());
        }

        @Test
        @DisplayName("Then CTA button link should be returned correctly")
        void shouldReturnCtaButtonLink() {
            assertEquals(TEST_CTA_LINK, model.getCtaButtonLink());
        }

        @Test
        @DisplayName("Then exported type should match resource type")
        void shouldReturnCorrectExportedType() {
            assertEquals(HeroModel.RESOURCE_TYPE, model.getExportedType());
        }
    }

    @Nested
    @DisplayName("Given a partially configured Hero component")
    class PartiallyConfiguredHero {

        @Test
        @DisplayName("When only heading is set, other fields should return null")
        void shouldHandlePartialConfiguration() {
            context.create().resource(RESOURCE_PATH + "-partial",
                "sling:resourceType", HeroModel.RESOURCE_TYPE,
                "heading", TEST_HEADING
            );

            Resource resource = context.resourceResolver().getResource(RESOURCE_PATH + "-partial");
            context.request().setResource(resource);
            HeroModel model = context.request().adaptTo(HeroModel.class);

            assertNotNull(model);
            assertEquals(TEST_HEADING, model.getHeading());
            assertNull(model.getSubheading(), "Subheading should be null when not configured");
            assertNull(model.getBackgroundImage(), "Background image should be null when not configured");
            assertNull(model.getCtaButtonText(), "CTA text should be null when not configured");
            assertNull(model.getCtaButtonLink(), "CTA link should be null when not configured");
        }
    }

    @Nested
    @DisplayName("Given an empty Hero component")
    class EmptyHero {

        @Test
        @DisplayName("Then all fields should return null")
        void shouldHandleEmptyComponent() {
            context.create().resource(RESOURCE_PATH + "-empty",
                "sling:resourceType", HeroModel.RESOURCE_TYPE
            );

            Resource resource = context.resourceResolver().getResource(RESOURCE_PATH + "-empty");
            context.request().setResource(resource);
            HeroModel model = context.request().adaptTo(HeroModel.class);

            assertNotNull(model, "Model should still be instantiated for empty component");
            assertNull(model.getHeading());
            assertNull(model.getSubheading());
            assertNull(model.getBackgroundImage());
            assertNull(model.getCtaButtonText());
            assertNull(model.getCtaButtonLink());
        }
    }

    @Nested
    @DisplayName("Given i18n content requirements")
    class InternationalizationSupport {

        @Test
        @DisplayName("Then heading should support unicode characters")
        void shouldSupportUnicodeInHeading() {
            String unicodeHeading = "Bienvenue sur notre site - 欢迎";
            context.create().resource(RESOURCE_PATH + "-i18n",
                "sling:resourceType", HeroModel.RESOURCE_TYPE,
                "heading", unicodeHeading
            );

            Resource resource = context.resourceResolver().getResource(RESOURCE_PATH + "-i18n");
            context.request().setResource(resource);
            HeroModel model = context.request().adaptTo(HeroModel.class);

            assertEquals(unicodeHeading, model.getHeading(),
                "Model should correctly handle unicode characters for i18n support");
        }

        @Test
        @DisplayName("Then CTA text should support special characters")
        void shouldSupportSpecialCharactersInCtaText() {
            String specialCharsText = "En savoir plus →";
            context.create().resource(RESOURCE_PATH + "-special",
                "sling:resourceType", HeroModel.RESOURCE_TYPE,
                "ctaButtonText", specialCharsText
            );

            Resource resource = context.resourceResolver().getResource(RESOURCE_PATH + "-special");
            context.request().setResource(resource);
            HeroModel model = context.request().adaptTo(HeroModel.class);

            assertEquals(specialCharsText, model.getCtaButtonText());
        }
    }

    @Nested
    @DisplayName("Given external URL requirements")
    class ExternalUrlSupport {

        @Test
        @DisplayName("Then CTA link should support external URLs")
        void shouldSupportExternalUrls() {
            String externalUrl = "https://www.example.com/landing-page";
            context.create().resource(RESOURCE_PATH + "-external",
                "sling:resourceType", HeroModel.RESOURCE_TYPE,
                "ctaButtonLink", externalUrl
            );

            Resource resource = context.resourceResolver().getResource(RESOURCE_PATH + "-external");
            context.request().setResource(resource);
            HeroModel model = context.request().adaptTo(HeroModel.class);

            assertEquals(externalUrl, model.getCtaButtonLink(),
                "Model should correctly return external URLs per AC4 of US-CA-003");
        }
    }
}
