package com.corp.pixmeup.auth.service;

import com.corp.pixmeup.global.sms.CoolSMSSender;
import com.corp.pixmeup.global.util.PhoneUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 메세지 발송 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final CoolSMSSender coolSMSSender;

    /**
     * 인증 코드를 포함한 메세지를 발송
     *
     * @param phoneNumber 수신자 전화번호
     * @param code 인증코드
     * @return 발송 성공 여부
     */
    public boolean sendVerificationCode(String phoneNumber, String code) {
        // 전화번호 형식 검증
        if (!PhoneUtils.isValidPhoneNumber(phoneNumber)) {
            log.error("Invalid phone number format: {}", phoneNumber);
            return false;
        }

        try {
            // CoolSMS를 사용하여 메세지 전송
            return coolSMSSender.sendVerificationCode(phoneNumber, code);
        } catch (Exception e) {
            log.error("Failed to send verification code: {}", e.getMessage());
            return false;
        }
    }

}