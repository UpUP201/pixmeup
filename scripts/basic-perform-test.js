import http from 'k6/http';
import { check } from 'k6';
import { TARGET_URL } from './common.js';

export const options = {
    stages: [
        { duration: '10s', target: 250 },
        { duration: '60s', target: 250 },
        { duration: '10s', target: 0 },
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<300'],
        checks: ['rate>0.99'],
        iterations: ['rate>1000']
    },
};

const HEALTH_ENDPOINT = TARGET_URL + "/api/v1/loadtest/ping";

export default function () {
    const res = http.get(HEALTH_ENDPOINT);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}