/**
 * Navigation Component JavaScript
 * Specification: SPEC-NAV-001
 */

class Navigation {
    constructor(element) {
        this.element = element;
        this.toggle = element.querySelector('.cmp-navigation__toggle');
        this.isOpen = false;

        this.init();
    }

    init() {
        if (this.toggle) {
            this.toggle.addEventListener('click', () => this.toggleMenu());
        }

        // Close menu when clicking outside
        document.addEventListener('click', (e) => {
            if (this.isOpen && !this.element.contains(e.target)) {
                this.closeMenu();
            }
        });

        // Close menu on escape key
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && this.isOpen) {
                this.closeMenu();
                this.toggle?.focus();
            }
        });

        // Handle keyboard navigation in dropdowns
        this.element.querySelectorAll('.cmp-navigation__item--level-0').forEach(item => {
            const link = item.querySelector('.cmp-navigation__link');
            const submenu = item.querySelector('.cmp-navigation__list');

            if (link && submenu) {
                link.addEventListener('keydown', (e) => {
                    if (e.key === 'ArrowDown') {
                        e.preventDefault();
                        const firstSubLink = submenu.querySelector('.cmp-navigation__link');
                        firstSubLink?.focus();
                    }
                });
            }
        });
    }

    toggleMenu() {
        this.isOpen ? this.closeMenu() : this.openMenu();
    }

    openMenu() {
        this.isOpen = true;
        this.element.classList.add('cmp-navigation--open');
        this.toggle?.setAttribute('aria-expanded', 'true');
    }

    closeMenu() {
        this.isOpen = false;
        this.element.classList.remove('cmp-navigation--open');
        this.toggle?.setAttribute('aria-expanded', 'false');
    }
}

// Auto-initialize all navigation components
document.querySelectorAll('.cmp-navigation').forEach(el => new Navigation(el));

export default Navigation;
