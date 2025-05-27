// api/v1/auth/login 로그인 테스트
import http from 'k6/http';
import { check } from 'k6';
import { TARGET_URL } from './common.js';

export const options = {
    stages: [
        { duration: '10s', target: 37 },
        { duration: '60s', target: 37 },
        { duration: '10s', target: 0 },
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<1500'],
        checks: ['rate>0.99'],
        iterations: ['rate>30'],
    },
};

const ENDPOINT = `${TARGET_URL}/api/v1/auth/login`;

export default function () {
    const vuIdBasedSuffix = (__VU).toString().padStart(8, '0');
    const middlePart = vuIdBasedSuffix.substring(0, 4);
    const lastPart = vuIdBasedSuffix.substring(4, 8);
    const createdPhoneNumber = `010-${middlePart}-${lastPart}`;
  
    const payload = JSON.stringify({
      phoneNumber: createdPhoneNumber,
      password: '12345678',
    });
  
    const params = {
      headers: {
        'Content-Type': 'application/json',
      },
    };
  
    const res = http.post(ENDPOINT, payload, params);
  
    let parsed;
    try {
        parsed = res.json();
    } catch (_) {
        parsed = null;
    }

    check(res, {
        'status is 200': (r) => r.status === 200,
        'contains accessToken': () =>
            parsed?.data?.accessToken?.length > 0,
    });
}
