package com.corp.pixelro.global.sms;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CoolSMS API를 사용하여 SMS 및 알림톡을 발송하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CoolSMSSender implements MessageSender {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.sender.phone}")
    private String senderPhone;

    @Value("${coolsms.sender.domain}")
    private String domain;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        // CoolSMS SDK 초기화
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, domain);
    }

    @Override
    public boolean sendVerificationCode(String phoneNumber, String code) {
        return sendPhoneVerificationCode(phoneNumber, code);
    }

    /**
     * 휴대전화 인증번호 발송 메서드
     */
    private boolean sendPhoneVerificationCode(String phoneNumber, String code) {
        try {
            Message message = new Message();
            message.setFrom(senderPhone);
            message.setTo(phoneNumber);
            message.setText(String.format("[픽미업] 인증번호 %s를 입력해주세요. 본인 확인을 위해 전송된 메시지입니다.", code));

            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
            log.info("SMS sent successfully: {}", response);
            return true;

        } catch (Exception e) {
            log.error("Failed to send SMS: {}", e.getMessage());
            return false;
        }
    }
}