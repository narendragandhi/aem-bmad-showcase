/**
 * Carousel Component JavaScript
 * Specification: SPEC-CAROUSEL-001
 */

class Carousel {
    constructor(element) {
        this.element = element;
        this.slides = element.querySelectorAll('.cmp-carousel__slide');
        this.slidesContainer = element.querySelector('.cmp-carousel__slides');
        this.indicators = element.querySelectorAll('.cmp-carousel__indicator');
        this.prevBtn = element.querySelector('.cmp-carousel__arrow--prev');
        this.nextBtn = element.querySelector('.cmp-carousel__arrow--next');
        this.pauseBtn = element.querySelector('.cmp-carousel__pause');

        this.currentIndex = 0;
        this.autoplay = element.dataset.autoplay === 'true';
        this.autoplaySpeed = parseInt(element.dataset.autoplaySpeed) || 5000;
        this.pauseOnHover = element.dataset.pauseOnHover !== 'false';
        this.autoplayInterval = null;
        this.isPaused = false;

        this.init();
    }

    init() {
        if (this.slides.length <= 1) return;

        this.bindEvents();

        if (this.autoplay) {
            this.startAutoplay();
        }

        this.updateAriaAttributes();
    }

    bindEvents() {
        if (this.prevBtn) {
            this.prevBtn.addEventListener('click', () => this.prev());
        }

        if (this.nextBtn) {
            this.nextBtn.addEventListener('click', () => this.next());
        }

        this.indicators.forEach((indicator, index) => {
            indicator.addEventListener('click', () => this.goTo(index));
        });

        if (this.pauseBtn) {
            this.pauseBtn.addEventListener('click', () => this.togglePause());
        }

        if (this.pauseOnHover) {
            this.element.addEventListener('mouseenter', () => this.pause());
            this.element.addEventListener('mouseleave', () => {
                if (!this.isPaused) this.startAutoplay();
            });
        }

        // Keyboard navigation
        this.element.addEventListener('keydown', (e) => {
            if (e.key === 'ArrowLeft') this.prev();
            if (e.key === 'ArrowRight') this.next();
        });
    }

    goTo(index) {
        if (index < 0) index = this.slides.length - 1;
        if (index >= this.slides.length) index = 0;

        this.currentIndex = index;
        this.slidesContainer.style.transform = `translateX(-${index * 100}%)`;

        this.updateIndicators();
        this.updateAriaAttributes();
    }

    prev() {
        this.goTo(this.currentIndex - 1);
    }

    next() {
        this.goTo(this.currentIndex + 1);
    }

    startAutoplay() {
        if (this.autoplayInterval) return;

        this.autoplayInterval = setInterval(() => {
            this.next();
        }, this.autoplaySpeed);
    }

    pause() {
        if (this.autoplayInterval) {
            clearInterval(this.autoplayInterval);
            this.autoplayInterval = null;
        }
    }

    togglePause() {
        this.isPaused = !this.isPaused;

        if (this.isPaused) {
            this.pause();
            if (this.pauseBtn) this.pauseBtn.textContent = 'Play';
        } else {
            this.startAutoplay();
            if (this.pauseBtn) this.pauseBtn.textContent = 'Pause';
        }
    }

    updateIndicators() {
        this.indicators.forEach((indicator, index) => {
            indicator.classList.toggle('cmp-carousel__indicator--active', index === this.currentIndex);
            indicator.setAttribute('aria-selected', index === this.currentIndex);
        });
    }

    updateAriaAttributes() {
        this.slides.forEach((slide, index) => {
            slide.setAttribute('aria-hidden', index !== this.currentIndex);
        });
    }
}

// Auto-initialize all carousel components
document.querySelectorAll('.cmp-carousel').forEach(el => new Carousel(el));

export default Carousel;
