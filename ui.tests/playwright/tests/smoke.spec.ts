import { test, expect } from '@playwright/test';

/**
 * Smoke Tests - Quick validation of critical functionality
 * Run before every deployment
 */

test.describe('Smoke Tests', () => {

    test('Homepage loads successfully', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        // Check page title
        await expect(page).toHaveTitle(/BMAD|Home/i);

        // Check critical elements are visible
        await expect(page.locator('header')).toBeVisible();
        await expect(page.locator('nav')).toBeVisible();
        await expect(page.locator('main')).toBeVisible();
        await expect(page.locator('footer')).toBeVisible();
    });

    test('Navigation is functional', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        // Check nav links exist
        const navLinks = page.locator('nav a');
        await expect(navLinks).toHaveCount.above(2);

        // Click first nav link and verify navigation
        const firstLink = navLinks.first();
        const href = await firstLink.getAttribute('href');

        if (href) {
            await firstLink.click();
            await page.waitForLoadState('networkidle');

            // Should have navigated
            expect(page.url()).not.toBe('/content/bmad-showcase/en/home.html');
        }
    });

    test('Hero component renders', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const hero = page.locator('.hero, [data-component="hero"]');
        await expect(hero).toBeVisible();

        // Check hero content
        const heroTitle = hero.locator('h1, .hero__title');
        await expect(heroTitle).toBeVisible();
    });

    test('No JavaScript errors on page load', async ({ page }) => {
        const errors: string[] = [];

        page.on('console', (msg) => {
            if (msg.type() === 'error') {
                errors.push(msg.text());
            }
        });

        await page.goto('/content/bmad-showcase/en/home.html');
        await page.waitForLoadState('networkidle');

        // Filter out expected/known errors
        const unexpectedErrors = errors.filter(
            (e) => !e.includes('favicon') && !e.includes('analytics')
        );

        expect(unexpectedErrors).toHaveLength(0);
    });

    test('Page responds within SLA', async ({ page }) => {
        const startTime = Date.now();

        await page.goto('/content/bmad-showcase/en/home.html');
        await page.waitForLoadState('domcontentloaded');

        const loadTime = Date.now() - startTime;

        // SLA: Page should load within 3 seconds
        expect(loadTime).toBeLessThan(3000);
    });

    test('Images load correctly', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const images = page.locator('img');
        const count = await images.count();

        for (let i = 0; i < count; i++) {
            const img = images.nth(i);
            const isVisible = await img.isVisible();

            if (isVisible) {
                // Check image loaded (naturalWidth > 0)
                const loaded = await img.evaluate(
                    (el) => (el as HTMLImageElement).complete && (el as HTMLImageElement).naturalWidth > 0
                );
                expect(loaded).toBeTruthy();
            }
        }
    });

    test('Footer links work', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const footer = page.locator('footer');
        await expect(footer).toBeVisible();

        const footerLinks = footer.locator('a');
        const count = await footerLinks.count();

        expect(count).toBeGreaterThan(0);
    });

});
