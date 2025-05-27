package com.corp.pixelro.global.config;

import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        log.info("Authorization header: {}", authHeader);

        String token = jwtTokenProvider.resolveToken(request);
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 디버깅을 위한 로그 추가
        log.info("Request URI: {}", uri);
        log.info("Request Method: {}", method);
        log.info("Token present: {}", (token != null));

        // OPTIONS 요청은 항상 허용
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("[JwtAuthenticationFilter] URI: {}", uri);

        // 인증이 필요없는 URI 패턴 목록
        List<String> permitAllUriPatterns = Arrays.asList(
                "/api/v1/sample",
                "/api/v1/auth/verify-code",
                "/api/v1/auth/send-code",
                "/api/v1/auth/login",
                "/api/v1/auth/register",
                "/api/v1/auth/refresh-token",
                "/api/v1/webauthn/register/start",
                "/api/v1/webauthn/register/finish",
                "/api/v1/webauthn/authenticate/start",
                "/api/v1/webauthn/authenticate/finish",
                "/error",
                "/favicon.ico",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/api/v1/swagger-ui.html",
                "/api/v1/swagger-ui/index.html",
                "/api/v1/swagger-ui/**",
                "/api/v1/api-docs/**",
                "/api/v1/swagger-resources/**",
                "/api/v1/webjars/**",
                "/api/v2/**",
                "/v3/api-docs/**",
                "/actuator/health",
                "/actuator/info",
                "/actuator/prometheus",
                "/api/v1/loadtest/**"
        );

        // 패턴 매칭 확인 (경로 와일드카드 처리)
        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean isPermitAllTarget = permitAllUriPatterns.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));

        if (isPermitAllTarget) {
            log.info("[JwtAuthenticationFilter] PermitAll URI: {}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        // 인증이 필요한 경로에 대한 처리
        log.info("[JwtAuthenticationFilter] Needs Authentication URI: {}", uri);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 Security Context에 인증 정보를 등록
            // jwtTokenProvider.getAuthentication(token)은 CustomUserDetails를 Principal로 하는 Authentication 객체를 반환해야 함
            Authentication auth = jwtTokenProvider.getAuthentication(token);

            if (auth != null) { // getAuthentication이 null을 반환할 경우를 대비 (예: UserDetails 조회 실패)
                // 일반적으로 UsernamePasswordAuthenticationToken의 details에 request 정보를 설정
                if (auth instanceof UsernamePasswordAuthenticationToken) {
                    ((UsernamePasswordAuthenticationToken) auth).setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                }
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("[JwtAuthenticationFilter] Authentication set for URI: " + uri);
            } else {
                // 인증 객체를 얻지 못한 경우 (예: 토큰은 유효하나 사용자 정보 못 찾음)
                sendErrorResponse(response, ErrorCode.USER_NOT_FOUND);
                return;
            }
        } else {
            // 토큰이 없거나 유효하지 않은 경우 (만료, 위조, 블랙리스트 등)
            log.info("[JwtAuthenticationFilter] Invalid or missing token for URI: " + uri);
            if (token == null) {
                sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
            } else {
                sendErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
            }
            return;
        }

        // 모든 검증 통과 후 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        if (errorCode == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"message\": \"Error code is missing.\"}");
            return;
        }

        // 다음은 ErrorCode.java에 @Getter가 있고 Lombok이 정상 동작할 때의 코드
        String errorResponseBody = String.format(
                "{" +
                        " \"status\": %d," +
                        " \"name\": \"%s\"," +
                        " \"message\": \"%s\"" +
                        "}",
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getMessage()
        );

        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(errorResponseBody);
    }
}