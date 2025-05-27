import http from 'k6/http';
import { check } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';
import { TARGET_URL, commonOptions } from './common.js';

export const options = commonOptions;

const API_BASE_PATH = '/api/v1/loadtest/users/';

const MIN_USER_ID = 1;
const MAX_USER_ID = 100000;

export default function () {
    const userId = randomIntBetween(MIN_USER_ID, MAX_USER_ID);

    const res = http.get(`${TARGET_URL}${API_BASE_PATH}${userId}`,{
            tags: {
                name: `${TARGET_URL}${API_BASE_PATH}{userId}`,
            },
        }
    );

    const checkRes = check(res, {
        'status is 200': (r) => r.status === 200,
        'response body is not empty': (r) => r.body && r.body.length > 0,
    });

    if (!checkRes || res.status !== 200) {
        console.log(`Request to ${res.request.url} failed. Status: ${res.status}, VU: ${__VU}, ITER: ${__ITER}, Error: ${res.error}, Body: ${res.body}`);
    }
}