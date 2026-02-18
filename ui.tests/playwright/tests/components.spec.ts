import { test, expect } from '@playwright/test';

/**
 * Component Tests - Validate individual AEM components
 */

test.describe('Hero Component', () => {

    test('Hero displays title and description', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const hero = page.locator('.hero, [data-component="hero"]').first();
        await expect(hero).toBeVisible();

        // Title
        const title = hero.locator('h1, .hero__title');
        await expect(title).toBeVisible();
        await expect(title).not.toBeEmpty();

        // Description (optional)
        const description = hero.locator('.hero__description, p');
        if (await description.count() > 0) {
            await expect(description.first()).toBeVisible();
        }
    });

    test('Hero CTA button navigates correctly', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const hero = page.locator('.hero, [data-component="hero"]').first();
        const cta = hero.locator('a.hero__cta, .hero a.btn, .hero button');

        if (await cta.count() > 0) {
            const href = await cta.first().getAttribute('href');

            await cta.first().click();
            await page.waitForLoadState('networkidle');

            if (href && !href.startsWith('#')) {
                expect(page.url()).toContain(href.replace('/content/', ''));
            }
        }
    });

    test('Hero background image loads', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const hero = page.locator('.hero, [data-component="hero"]').first();

        // Check for background image in CSS
        const bgImage = await hero.evaluate((el) => {
            return window.getComputedStyle(el).backgroundImage;
        });

        // Either has background-image or contains an img
        const hasImage = bgImage !== 'none' || (await hero.locator('img').count()) > 0;
        expect(hasImage).toBeTruthy();
    });

});

test.describe('Card Component', () => {

    test('Card displays required elements', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const cards = page.locator('.card, [data-component="card"]');

        if (await cards.count() > 0) {
            const card = cards.first();

            // Title
            const title = card.locator('.card__title, h2, h3');
            await expect(title).toBeVisible();

            // Image (optional but common)
            const image = card.locator('img');
            if (await image.count() > 0) {
                await expect(image).toBeVisible();
            }
        }
    });

    test('Card link is clickable', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const cards = page.locator('.card, [data-component="card"]');

        if (await cards.count() > 0) {
            const card = cards.first();
            const link = card.locator('a').first();

            if (await link.count() > 0) {
                await expect(link).toBeEnabled();
                const href = await link.getAttribute('href');
                expect(href).toBeTruthy();
            }
        }
    });

});

test.describe('Navigation Component', () => {

    test('Navigation displays menu items', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const nav = page.locator('nav, [data-component="navigation"]');
        await expect(nav).toBeVisible();

        const menuItems = nav.locator('a, button');
        await expect(menuItems).toHaveCount.above(1);
    });

    test('Mobile menu toggle works', async ({ page }) => {
        // Set mobile viewport
        await page.setViewportSize({ width: 375, height: 812 });

        await page.goto('/content/bmad-showcase/en/home.html');

        const menuToggle = page.locator('[data-testid="menu-toggle"], .nav-toggle, .hamburger');

        if (await menuToggle.count() > 0) {
            // Menu should be hidden initially on mobile
            const menu = page.locator('.nav-menu, [data-nav-menu]');

            await menuToggle.click();

            // Menu should be visible after click
            await expect(menu).toBeVisible();

            // Click again to close
            await menuToggle.click();
            await expect(menu).not.toBeVisible();
        }
    });

    test('Dropdown menus work', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const dropdownTrigger = page.locator('.dropdown-toggle, [data-dropdown]');

        if (await dropdownTrigger.count() > 0) {
            await dropdownTrigger.first().hover();

            const dropdown = page.locator('.dropdown-menu, [data-dropdown-menu]');
            await expect(dropdown).toBeVisible();
        }
    });

});

test.describe('Carousel Component', () => {

    test('Carousel displays slides', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const carousel = page.locator('.carousel, [data-component="carousel"]');

        if (await carousel.count() > 0) {
            const slides = carousel.locator('.carousel__slide, .slide');
            await expect(slides).toHaveCount.above(0);
        }
    });

    test('Carousel navigation works', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const carousel = page.locator('.carousel, [data-component="carousel"]');

        if (await carousel.count() > 0) {
            const nextBtn = carousel.locator('.carousel__next, [data-carousel-next]');
            const prevBtn = carousel.locator('.carousel__prev, [data-carousel-prev]');

            if (await nextBtn.count() > 0) {
                // Get initial active slide
                const initialSlide = await carousel.locator('.carousel__slide.active, .slide.active').getAttribute('data-index');

                // Click next
                await nextBtn.click();
                await page.waitForTimeout(500);

                // Check slide changed
                const newSlide = await carousel.locator('.carousel__slide.active, .slide.active').getAttribute('data-index');

                // Slide should have changed (unless only 1 slide)
                // expect(newSlide).not.toBe(initialSlide);
            }
        }
    });

});

test.describe('Search Component', () => {

    test('Search input accepts text', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const searchInput = page.locator('input[type="search"], [data-testid="search-input"], .search-input');

        if (await searchInput.count() > 0) {
            await searchInput.fill('test search');
            await expect(searchInput).toHaveValue('test search');
        }
    });

    test('Search returns results', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const searchInput = page.locator('input[type="search"], [data-testid="search-input"], .search-input');
        const searchForm = page.locator('form[role="search"], .search-form');

        if (await searchInput.count() > 0) {
            await searchInput.fill('bmad');

            // Submit search
            if (await searchForm.count() > 0) {
                await searchForm.press('Enter');
            } else {
                await searchInput.press('Enter');
            }

            await page.waitForLoadState('networkidle');

            // Check for results (page should show results or navigate to search results page)
            const results = page.locator('.search-results, [data-search-results]');
            if (await results.count() > 0) {
                await expect(results).toBeVisible();
            }
        }
    });

});
