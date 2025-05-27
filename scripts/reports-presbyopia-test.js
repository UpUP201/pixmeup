// GET api/v1/reports/presbyopia 노안 검사 상세 조회 테스트
import http from 'k6/http';
import { check, fail } from 'k6';
import { TARGET_URL, commonOptions, getAuthHeaders, tokens } from './common.js';

export const options = {
    ...commonOptions,
    setupTimeout: '180s',
};

const ENDPOINT = `${TARGET_URL}/api/v1/reports/presbyopia`;

export default function () {
    if (!tokens?.length) {
        fail('[Runtime] No tokens received from setup.');
    }

    const token = tokens[__VU % tokens.length];
    const headers = getAuthHeaders(token);

    const selectedDateTime = '2025-05-01T07:00:00';
    const url = `${ENDPOINT}?selectedDateTime=${encodeURIComponent(selectedDateTime)}`;

    const res = http.get(url, { headers: headers });

    const success = check(res, {
        'GET /presbyopia returns 200': (r) => r.status === 200,
    });

    if (!success && __ENV.DEBUG === 'true') {
        console.log(`[VU ${__VU}] GET failed. Status: ${res.status}, Body: ${res.body?.substring(0, 200)}`);
    }
}