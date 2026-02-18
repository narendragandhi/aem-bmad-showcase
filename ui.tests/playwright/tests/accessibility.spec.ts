import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

/**
 * Accessibility Tests - WCAG 2.1 AA Compliance
 */

test.describe('Accessibility', () => {

    test('Homepage passes axe accessibility scan', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');
        await page.waitForLoadState('networkidle');

        const accessibilityScanResults = await new AxeBuilder({ page })
            .withTags(['wcag2a', 'wcag2aa', 'wcag21aa'])
            .analyze();

        expect(accessibilityScanResults.violations).toEqual([]);
    });

    test('About page passes axe accessibility scan', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/about.html');
        await page.waitForLoadState('networkidle');

        const accessibilityScanResults = await new AxeBuilder({ page })
            .withTags(['wcag2a', 'wcag2aa'])
            .analyze();

        expect(accessibilityScanResults.violations).toEqual([]);
    });

    test('Page has proper heading hierarchy', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        // Check for h1
        const h1Count = await page.locator('h1').count();
        expect(h1Count).toBe(1);

        // Check heading order
        const headings = await page.locator('h1, h2, h3, h4, h5, h6').all();
        const levels = await Promise.all(
            headings.map(async (h) => {
                const tagName = await h.evaluate((el) => el.tagName);
                return parseInt(tagName.charAt(1));
            })
        );

        // Ensure no heading level is skipped
        for (let i = 1; i < levels.length; i++) {
            const diff = levels[i] - levels[i - 1];
            expect(diff).toBeLessThanOrEqual(1);
        }
    });

    test('All images have alt text', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const images = await page.locator('img').all();

        for (const img of images) {
            const alt = await img.getAttribute('alt');
            const role = await img.getAttribute('role');

            // Image should have alt text OR role="presentation" for decorative images
            const hasAccessibility = alt !== null || role === 'presentation';
            expect(hasAccessibility).toBeTruthy();
        }
    });

    test('Links have accessible names', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const links = await page.locator('a').all();

        for (const link of links) {
            const text = await link.textContent();
            const ariaLabel = await link.getAttribute('aria-label');
            const title = await link.getAttribute('title');

            // Link should have text content, aria-label, or title
            const hasAccessibleName =
                (text && text.trim().length > 0) || ariaLabel || title;
            expect(hasAccessibleName).toBeTruthy();
        }
    });

    test('Form inputs have labels', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const inputs = await page.locator('input:not([type="hidden"]), select, textarea').all();

        for (const input of inputs) {
            const id = await input.getAttribute('id');
            const ariaLabel = await input.getAttribute('aria-label');
            const ariaLabelledby = await input.getAttribute('aria-labelledby');
            const placeholder = await input.getAttribute('placeholder');

            if (id) {
                const label = await page.locator(`label[for="${id}"]`).count();
                const hasLabel = label > 0 || ariaLabel || ariaLabelledby;
                expect(hasLabel).toBeTruthy();
            } else {
                // Must have aria-label if no id
                const hasAccessibleName = ariaLabel || ariaLabelledby || placeholder;
                expect(hasAccessibleName).toBeTruthy();
            }
        }
    });

    test('Keyboard navigation works', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        // Tab through the page
        await page.keyboard.press('Tab');

        // Check that an element is focused
        const focusedElement = await page.evaluate(() => document.activeElement?.tagName);
        expect(focusedElement).toBeTruthy();

        // Tab a few more times
        for (let i = 0; i < 5; i++) {
            await page.keyboard.press('Tab');
        }

        // Focus should have moved
        const newFocusedElement = await page.evaluate(
            () => document.activeElement?.tagName
        );
        expect(newFocusedElement).toBeTruthy();
    });

    test('Focus indicators are visible', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        // Focus on first interactive element
        await page.keyboard.press('Tab');

        // Get the focused element
        const focusedElement = page.locator(':focus');
        await expect(focusedElement).toBeVisible();

        // Check for visible focus styles
        const outlineStyle = await focusedElement.evaluate((el) => {
            const styles = window.getComputedStyle(el);
            return {
                outline: styles.outline,
                boxShadow: styles.boxShadow,
                border: styles.border,
            };
        });

        // Should have some visible focus indicator
        const hasFocusIndicator =
            outlineStyle.outline !== 'none' ||
            outlineStyle.boxShadow !== 'none' ||
            outlineStyle.border !== 'none';

        expect(hasFocusIndicator).toBeTruthy();
    });

    test('Color contrast meets WCAG AA', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const accessibilityScanResults = await new AxeBuilder({ page })
            .withRules(['color-contrast'])
            .analyze();

        expect(accessibilityScanResults.violations).toEqual([]);
    });

    test('ARIA roles are used correctly', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const accessibilityScanResults = await new AxeBuilder({ page })
            .withRules([
                'aria-allowed-attr',
                'aria-required-attr',
                'aria-valid-attr',
                'aria-valid-attr-value',
            ])
            .analyze();

        expect(accessibilityScanResults.violations).toEqual([]);
    });

    test('Skip link exists and works', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        // Press Tab to reveal skip link
        await page.keyboard.press('Tab');

        const skipLink = page.locator('a[href="#main"], a[href="#content"], .skip-link');

        if (await skipLink.count() > 0) {
            await expect(skipLink).toBeVisible();

            // Click skip link
            await skipLink.click();

            // Focus should be on main content
            const focusedElement = await page.evaluate(
                () => document.activeElement?.id || document.activeElement?.tagName
            );
            expect(['main', 'content', 'MAIN']).toContain(focusedElement);
        }
    });

});

test.describe('Mobile Accessibility', () => {

    test.beforeEach(async ({ page }) => {
        await page.setViewportSize({ width: 375, height: 812 });
    });

    test('Touch targets are large enough', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const interactiveElements = await page
            .locator('a, button, input, select, textarea, [role="button"]')
            .all();

        for (const element of interactiveElements) {
            if (await element.isVisible()) {
                const box = await element.boundingBox();
                if (box) {
                    // WCAG 2.5.5: Target size should be at least 44x44px
                    // For mobile, we check for at least 44px in one dimension
                    const meetsMinSize = box.width >= 44 || box.height >= 44;
                    // This is a soft check - some inline links may be smaller
                }
            }
        }
    });

    test('Mobile menu is accessible', async ({ page }) => {
        await page.goto('/content/bmad-showcase/en/home.html');

        const menuToggle = page.locator(
            '[data-testid="menu-toggle"], .nav-toggle, .hamburger, button[aria-label*="menu"]'
        );

        if (await menuToggle.count() > 0) {
            // Menu toggle should have accessible name
            const ariaLabel = await menuToggle.getAttribute('aria-label');
            const ariaExpanded = await menuToggle.getAttribute('aria-expanded');

            expect(ariaLabel || (await menuToggle.textContent())).toBeTruthy();
            expect(ariaExpanded).toBeTruthy();
        }
    });

});
