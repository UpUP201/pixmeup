import http from 'k6/http';
import { check, fail } from 'k6';
import { TARGET_URL, commonOptions, getAuthHeaders, tokens } from './common.js';

export const options = {
    ...commonOptions,
    setupTimeout: '180s',
};

const ENDPOINT = `${TARGET_URL}/api/v1/total-diagnosis`;

export default function () {
    const token = tokens[(__VU - 1) % tokens.length];
    const headers = getAuthHeaders(token);

    // page는 100 미만으로 랜덤 생성
    const page = Math.floor(Math.random() * 100);
    const size = 10;  // size는 10으로 고정

    const url = `${ENDPOINT}?page=${page}&size=${size}`;
    const res = http.get(url, { headers: headers });

    const success = check(res, {
        'GET /profile returns 200': (r) => r.status === 200,
    });

    if (!success && __ENV.DEBUG === 'true') {
        console.log(`[VU ${__VU}] GET failed. Status: ${res.status}, Body: ${res.body?.substring(0, 200)}`);
    }
}
