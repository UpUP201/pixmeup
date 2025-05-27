package com.corp.pixelro.global.config;

import java.util.List;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pixelro API Documentation")
                        .description("<h3>Pixelro Reference for Developers </h3>Swagger를 이용한 Pixelro API")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("dkdl8412@gmail.com")
                                .url("https://pixelro.com"))
                        .license(new License()
                                .name("Pixelro License")
                                .url("https://pixelro.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Pixelro Documentation")
                        .url("https://pixelro.com"))
                .servers(List.of(
                        new Server().url("").description("Local server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        ))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    /**
     * Swagger에서 @CurrentUser AuthUser authUser를 자동으로 제거하여 JSON 입력 없이 Bearer Token을 기반으로 인증 가능하게 설정
     */
    @Bean
    public OperationCustomizer customizeOperation() {
        return (operation, handlerMethod) -> {
            List<Parameter> parameters = operation.getParameters();
            if (parameters != null) {
                parameters.removeIf(param -> param.getName().equals("userDetails"));
            }
            return operation;
        };
    }
}
