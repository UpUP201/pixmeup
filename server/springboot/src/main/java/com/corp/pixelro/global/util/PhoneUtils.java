package com.corp.pixelro.global.util;

public class PhoneUtils {

    private PhoneUtils() {
        throw new IllegalStateException("유틸 클래스는 인스턴스화 되어선 안됩니다.");
    }

    /**
     * 전화번호 형식 검증
     * @param phoneNumber 검증할 전화번호
     * @return 유효성 여부
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // 한국 전화번호 형식 검증 (010-xxxx-xxxx 또는 010xxxxxxxx)
        return phoneNumber != null &&
                (phoneNumber.matches("^010-\\d{4}-\\d{4}$") ||
                        phoneNumber.matches("^010\\d{8}$"));
    }

    /**
     * 전화번호 형식 통일 (하이픈 제거)
     * @param phoneNumber 원본 전화번호
     * @return 정규화된 전화번호
     */
    public static String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.replace("-", "");
    }

    /**
     * 전화번호에 하이픈("-")을 넣어 반환. 형식 안 맞으면 원본 반환.
     */
    public static String formatWithHyphen(String rawPhone) {
        if (rawPhone == null) return null;
        String onlyDigits = rawPhone.replaceAll("[^\\d]", "");
        if (onlyDigits.length() == 11) {
            return onlyDigits.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        } else if (onlyDigits.length() == 10) {
            return onlyDigits.replaceFirst("(\\d{2,3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
        }
        return rawPhone;
    }


    /**
     * UUID를 사용하여 절대 겹치지 않는 전화번호 대체값 생성
     * 형식: "WD-" + 짧은 UUID + "-" + 사용자 ID
     */
    public static String generateRandomWithdrawnPhoneNumber(Long userId) {
        // UUID의 일부(처음 8자)와 사용자 ID를 조합하여 고유한 값 생성
        String uniqueId = java.util.UUID.randomUUID().toString().substring(0, 8);
        return String.format("WD-%s-%d", uniqueId, userId);
    }

}
