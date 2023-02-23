package nextstep.config.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_URL = "/members/me";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(AUTH_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization header에서 JWT 토큰을 가져온다.
        String token = parseJwtToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (!jwtTokenProvider.validateToken(token)) {
            log.debug("유효하지 않은 토큰 : [{}]", token);
            throw new InvalidParameterException("유효하지 않은 jwt 토큰 입니다.");
        }

        request.setAttribute("email", jwtTokenProvider.getPrincipal(token));
        filterChain.doFilter(request, response);
    }

    private String parseJwtToken(String header) {
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("jwt 인증 헤더 정보가 유효하지 않습니다");
        }

        return header.substring(BEARER_PREFIX.length());
    }
}
