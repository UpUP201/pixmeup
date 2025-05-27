import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 1, // 1명의 가상 사용자
  duration: '10s', // 10초 동안 실행
  thresholds: {
    http_req_failed: ['rate<0.01'], // HTTP 실패율이 1% 미만이어야 함
    http_req_duration: ['p(95)<200'], // 95%의 요청이 200ms 이내에 완료되어야 함
  },
};

// 테스트 대상 URL (springboot-test 서비스의 컨테이너 내부 주소)
const TARGET_URL = 'http://springboot-test:8088/actuator/health';

export default function () {
  // HTTP GET 요청 보내기
  const res = http.get(TARGET_URL);

  // 응답 상태 코드 확인
  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  // 요청 사이에 잠시 대기 (1초)
  sleep(1);
}
