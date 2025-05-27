// GET api/v1/reports/total-list 검사 통합 조회 테스트
import http from 'k6/http';
import { check, fail } from 'k6';
import { TARGET_URL, commonOptions, getAuthHeaders, tokens } from './common.js';

export const options = {
    ...commonOptions,
    setupTimeout: '180s',
};

const ENDPOINT = `${TARGET_URL}/api/v1/reports/total-list`;

export default function () {
    if (!tokens?.length) {
        fail('[Runtime] No tokens received from setup.');
    }

    const token = tokens[__VU % tokens.length];
    const headers = getAuthHeaders(token);

    const res = http.get(ENDPOINT, { headers: headers });

    const success = check(res, {
        'GET /total-list returns 200': (r) => r.status === 200,
    });

    if (!success && __ENV.DEBUG === 'true') {
        console.log(`[VU ${__VU}] GET failed. Status: ${res.status}, Body: ${res.body?.substring(0, 200)}`);
    }
}
