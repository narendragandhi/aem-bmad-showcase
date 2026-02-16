/**
 * AEM BMAD Showcase - Main Frontend Entry Point
 *
 * This file imports all component JavaScript and styles,
 * serving as the entry point for Webpack bundling.
 */

// Styles
import './main.scss';

// Components
import './components/hero/hero';
import './components/card/card';
import './components/carousel/carousel';
import './components/navigation/navigation';

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', () => {
    console.log('AEM BMAD Showcase: Frontend initialized');

    // Initialize any global functionality here
    initAccessibility();
});

/**
 * Initialize accessibility enhancements
 */
function initAccessibility() {
    // Add focus-visible polyfill behavior
    document.body.addEventListener('keydown', (e) => {
        if (e.key === 'Tab') {
            document.body.classList.add('user-is-tabbing');
        }
    });

    document.body.addEventListener('mousedown', () => {
        document.body.classList.remove('user-is-tabbing');
    });
}
