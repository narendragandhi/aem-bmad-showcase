/**
 * Hero Component JavaScript
 * Specification: SPEC-HERO-001
 */

class Hero {
    constructor(element) {
        this.element = element;
        this.init();
    }

    init() {
        // Add lazy loading for background images
        const bgImage = this.element.querySelector('.cmp-hero__background img');
        if (bgImage && 'loading' in HTMLImageElement.prototype) {
            bgImage.loading = 'lazy';
        }

        // Initialize any video backgrounds
        const video = this.element.querySelector('.cmp-hero__video');
        if (video) {
            this.initVideoBackground(video);
        }
    }

    initVideoBackground(video) {
        // Pause video when not in viewport (performance)
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    video.play();
                } else {
                    video.pause();
                }
            });
        }, { threshold: 0.25 });

        observer.observe(this.element);
    }
}

// Auto-initialize all hero components
document.querySelectorAll('.cmp-hero').forEach(el => new Hero(el));

export default Hero;
