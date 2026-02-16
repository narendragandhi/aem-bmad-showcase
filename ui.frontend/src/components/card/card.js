/**
 * Card Component JavaScript
 * Specification: SPEC-CARD-001
 */

class Card {
    constructor(element) {
        this.element = element;
        this.init();
    }

    init() {
        // Make entire card clickable if it has a CTA link
        const ctaLink = this.element.querySelector('.cmp-card__cta');
        if (ctaLink) {
            this.element.style.cursor = 'pointer';
            this.element.addEventListener('click', (e) => {
                // Don't trigger if clicking directly on the link
                if (e.target !== ctaLink && !ctaLink.contains(e.target)) {
                    ctaLink.click();
                }
            });
        }
    }
}

// Auto-initialize all card components
document.querySelectorAll('.cmp-card').forEach(el => new Card(el));

export default Card;
