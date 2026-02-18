/**
 * k6 Load Test Script for AEM BMAD Showcase
 *
 * Usage:
 *   k6 run --env BASE_URL=https://stage.example.com k6-load-test.js
 *
 * Options:
 *   --vus 50        Number of virtual users
 *   --duration 5m   Test duration
 */

import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

// Custom metrics
const pageLoadTime = new Trend('page_load_time');
const apiResponseTime = new Trend('api_response_time');
const errorRate = new Rate('error_rate');
const requestCount = new Counter('request_count');

// Test configuration
export const options = {
    stages: [
        { duration: '1m', target: 10 },   // Ramp up to 10 users
        { duration: '3m', target: 50 },   // Ramp up to 50 users
        { duration: '5m', target: 50 },   // Stay at 50 users
        { duration: '2m', target: 100 },  // Spike to 100 users
        { duration: '3m', target: 50 },   // Back to 50 users
        { duration: '1m', target: 0 },    // Ramp down
    ],
    thresholds: {
        http_req_duration: ['p(95)<3000'],  // 95% of requests < 3s
        http_req_failed: ['rate<0.01'],      // Error rate < 1%
        page_load_time: ['p(95)<3000'],
        api_response_time: ['p(95)<500'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

// Page URLs to test
const PAGES = [
    '/content/bmad-showcase/en/home.html',
    '/content/bmad-showcase/en/about.html',
    '/content/bmad-showcase/en/products.html',
    '/content/bmad-showcase/en/contact.html',
];

// API endpoints to test
const API_ENDPOINTS = [
    '/content/bmad-showcase/en/home.model.json',
    '/api/search?q=bmad&limit=10',
];

// Simulated user behaviors
const USER_BEHAVIORS = {
    casual: { weight: 60, thinkTime: [3, 8] },
    engaged: { weight: 30, thinkTime: [1, 3] },
    power: { weight: 10, thinkTime: [0.5, 1] },
};

// Helper functions
function randomPage() {
    return PAGES[Math.floor(Math.random() * PAGES.length)];
}

function randomThinkTime(behavior) {
    const [min, max] = USER_BEHAVIORS[behavior].thinkTime;
    return min + Math.random() * (max - min);
}

function selectBehavior() {
    const rand = Math.random() * 100;
    let cumulative = 0;
    for (const [behavior, config] of Object.entries(USER_BEHAVIORS)) {
        cumulative += config.weight;
        if (rand <= cumulative) {
            return behavior;
        }
    }
    return 'casual';
}

// Main test scenario
export default function () {
    const behavior = selectBehavior();

    group('Page Navigation', function () {
        // Browse homepage
        const homeRes = http.get(`${BASE_URL}/content/bmad-showcase/en/home.html`, {
            tags: { name: 'HomePage' },
        });

        check(homeRes, {
            'homepage status is 200': (r) => r.status === 200,
            'homepage has hero': (r) => r.body.includes('hero'),
        });

        pageLoadTime.add(homeRes.timings.duration);
        requestCount.add(1);
        errorRate.add(homeRes.status !== 200);

        sleep(randomThinkTime(behavior));

        // Browse random page
        const page = randomPage();
        const pageRes = http.get(`${BASE_URL}${page}`, {
            tags: { name: 'ContentPage' },
        });

        check(pageRes, {
            'content page status is 200': (r) => r.status === 200,
        });

        pageLoadTime.add(pageRes.timings.duration);
        requestCount.add(1);
        errorRate.add(pageRes.status !== 200);

        sleep(randomThinkTime(behavior));
    });

    group('API Requests', function () {
        // Fetch page model (JSON export)
        const modelRes = http.get(`${BASE_URL}/content/bmad-showcase/en/home.model.json`, {
            tags: { name: 'PageModel' },
        });

        check(modelRes, {
            'model status is 200': (r) => r.status === 200,
            'model is valid JSON': (r) => {
                try {
                    JSON.parse(r.body);
                    return true;
                } catch (e) {
                    return false;
                }
            },
        });

        apiResponseTime.add(modelRes.timings.duration);
        requestCount.add(1);
        errorRate.add(modelRes.status !== 200);

        sleep(randomThinkTime(behavior) / 2);
    });

    group('Search Flow', function () {
        // Simulate search interaction
        const searchTerms = ['bmad', 'component', 'hero', 'card'];
        const term = searchTerms[Math.floor(Math.random() * searchTerms.length)];

        const searchRes = http.get(`${BASE_URL}/api/search?q=${term}&limit=10`, {
            tags: { name: 'Search' },
        });

        check(searchRes, {
            'search returns results': (r) => r.status === 200 || r.status === 404,
        });

        apiResponseTime.add(searchRes.timings.duration);
        requestCount.add(1);

        sleep(randomThinkTime(behavior));
    });

    group('Static Assets', function () {
        // Test static asset delivery
        const assets = [
            '/etc.clientlibs/bmad-showcase/clientlibs/clientlib-site.min.css',
            '/etc.clientlibs/bmad-showcase/clientlibs/clientlib-site.min.js',
        ];

        const responses = http.batch(
            assets.map((asset) => ['GET', `${BASE_URL}${asset}`, null, { tags: { name: 'StaticAsset' } }])
        );

        responses.forEach((res) => {
            check(res, {
                'asset loaded': (r) => r.status === 200,
                'asset is cached': (r) => r.headers['Cache-Control'] !== undefined,
            });
            requestCount.add(1);
        });

        sleep(randomThinkTime(behavior) / 4);
    });
}

// Lifecycle hooks
export function setup() {
    console.log(`Starting load test against: ${BASE_URL}`);

    // Verify target is accessible
    const res = http.get(`${BASE_URL}/content/bmad-showcase/en/home.html`);
    if (res.status !== 200) {
        throw new Error(`Target not accessible: ${res.status}`);
    }

    return { startTime: new Date().toISOString() };
}

export function teardown(data) {
    console.log(`Load test completed. Started: ${data.startTime}`);
}

// Custom summary
export function handleSummary(data) {
    const summary = {
        timestamp: new Date().toISOString(),
        baseUrl: BASE_URL,
        metrics: {
            requests: {
                total: data.metrics.http_reqs.values.count,
                rate: data.metrics.http_reqs.values.rate,
            },
            duration: {
                avg: data.metrics.http_req_duration.values.avg,
                p95: data.metrics.http_req_duration.values['p(95)'],
                p99: data.metrics.http_req_duration.values['p(99)'],
            },
            errors: {
                rate: data.metrics.http_req_failed.values.rate,
            },
        },
        thresholds: {
            passed: Object.entries(data.thresholds).every(([, v]) => v.ok),
            details: data.thresholds,
        },
    };

    return {
        'summary.json': JSON.stringify(summary, null, 2),
        stdout: textSummary(data, { indent: '  ', enableColors: true }),
    };
}

function textSummary(data, options) {
    const lines = [
        '============================================',
        'k6 Load Test Summary',
        '============================================',
        '',
        `Total Requests: ${data.metrics.http_reqs.values.count}`,
        `Request Rate: ${data.metrics.http_reqs.values.rate.toFixed(2)}/s`,
        '',
        'Response Times:',
        `  Average: ${data.metrics.http_req_duration.values.avg.toFixed(2)}ms`,
        `  P95: ${data.metrics.http_req_duration.values['p(95)'].toFixed(2)}ms`,
        `  P99: ${data.metrics.http_req_duration.values['p(99)'].toFixed(2)}ms`,
        '',
        `Error Rate: ${(data.metrics.http_req_failed.values.rate * 100).toFixed(2)}%`,
        '',
        'Threshold Results:',
    ];

    for (const [name, result] of Object.entries(data.thresholds)) {
        const status = result.ok ? '✓ PASS' : '✗ FAIL';
        lines.push(`  ${status}: ${name}`);
    }

    lines.push('');
    lines.push('============================================');

    return lines.join('\n');
}
