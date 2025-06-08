package com.corp.pixmeup.global.config;

/*
    Jackson 날짜/시간 포맷 설정 파일
    - LocalDate, LocalDateTime 직렬화 시 포맷 지정
    - 기본 ObjectMapper로 등록됨 (@Primary)
 */

import com.corp.pixmeup.global.util.NoHtmlCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
@ConfigurationProperties(prefix = "spring.custom")
@Setter
public class JsonDateFormatConfig {

   private String dateFormat;
   private String dateTimeFormat;

   @Value("${spring.jackson.time-zone}")
   private String timeZone;

    @Bean
    @Primary // ObjectMapper를 전역 기본값으로 사용
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.timeZone(TimeZone.getTimeZone(timeZone));
        builder.simpleDateFormat(dateTimeFormat);
        builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
        builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));

        builder.modules(new JavaTimeModule(), new Jdk8Module());

        // ✅ 중요: timestamp 직렬화 끔 (ISO-8601로 보냄)
        builder.featuresToDisable(
            com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ObjectMapper objectMapper = builder.build();

        // ✅ HTML 이스케이프 방지 설정
        objectMapper.getFactory().setCharacterEscapes(new NoHtmlCharacterEscapes());

        return objectMapper;
    }
}
