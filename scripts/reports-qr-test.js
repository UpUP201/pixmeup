import http from 'k6/http';
import { check, fail } from 'k6';
import { TARGET_URL, commonOptions, getAuthHeaders, tokens } from './common.js';

export const options = {
    ...commonOptions,
    setupTimeout: '180s',
};

const ENDPOINT = `${TARGET_URL}/api/v1/reports/qr`;

const payload = JSON.stringify({
    age: 2,
    currentSmoking: false,
    diabetes: false,
    gender: "W",
    glasses: false,
    pastSmoking: false,
    surgery: "etc",
    testResults: {
        shortVisualAcuity: {
            userId: 1,
            leftEye: 7,
            rightEye: 5,
            leftPerspective: "NEARSIGHTED",
            rightPerspective: "NEARSIGHTED",
            leftSightPrediction: 6,
            rightSightPrediction: 5,
            aiResult: "양안 시야 왜곡이 감지되었습니다.",
            status: "GOOD"
        },
        presbyopia: {
            userId: 1,
            firstDistance: 4.2,
            secondDistance: 5.1,
            thirdDistance: 3.8,
            avgDistance: 4.37,
            age: 62,
            agePrediction: 64,
            aiResult: "예측된 연령과 실제 연령이 유사하며, 거리 측정값은 안정적인 수준입니다.",
            status: "GOOD"
        },
        amslerGrid: {
            userId: 1,
            rightEyeDisorderType: ["n", "n", "b", "n", "w", "n", "n", "n", "n"],
            leftEyeDisorderType: ["n", "w", "n", "n", "b", "n", "n", "n", "n"],
            aiResult: "좌안 황반 부위에 일부 이상이 감지되었으며, 우안은 비교적 정상적인 패턴을 보입니다."
        },
        mChart: {
            userId: 1,
            leftEyeVertical: 2,
            rightEyeVertical: 3,
            leftEyeHorizontal: 1,
            rightEyeHorizontal: 2,
            aiResult: "좌안과 우안 모두 경미한 수직 및 수평 시야 이상이 감지되었습니다."
        }
    }
});

export default function () {
    if (!tokens?.length) {
        fail('[Runtime] No tokens received from setup.');
    }

    const token = tokens[0];
    const headers = getAuthHeaders(token);

    const res = http.post(ENDPOINT, payload, { headers: headers });

    const success = check(res, {
        'GET /total-list returns 200': (r) => r.status === 200,
    });

    if (!success && __ENV.DEBUG === 'true') {
        console.log(`[VU ${__VU}] GET failed. Status: ${res.status}, Body: ${res.body?.substring(0, 200)}`);
    }
}
