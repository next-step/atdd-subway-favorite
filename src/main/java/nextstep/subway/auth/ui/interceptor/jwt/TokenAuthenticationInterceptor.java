package nextstep.subway.auth.ui.interceptor.jwt;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService,
        JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return false;
    }

    public AuthenticationToken authenticate(HttpServletRequest request) {
        return null;
    }

    private Authentication authenticate(AuthenticationToken token) {
        LoginMember loginMember = customUserDetailsService.loadUserByUsername(token.getPrincipal());
        if (Objects.isNull(loginMember)) {
            throw new RuntimeException("there is no user.");
        }
        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new RuntimeException("password is wrong.");
        }
        return new Authentication(loginMember);
    }
}
