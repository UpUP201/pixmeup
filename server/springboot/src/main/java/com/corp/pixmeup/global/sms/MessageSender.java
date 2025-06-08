package com.corp.pixmeup.global.sms;

/**
 * 메세지 발송 서비스 인터페이스
 */
public interface MessageSender {

    /**
     * 전화번호 인증
     * 인증코드를 포함한 메세지를 발송
     * @param phoneNumber 수신자 전화번호
     * @param code 인증코드
     * @return 발송 성공 여부
     */
    boolean sendVerificationCode(String phoneNumber, String code);

}