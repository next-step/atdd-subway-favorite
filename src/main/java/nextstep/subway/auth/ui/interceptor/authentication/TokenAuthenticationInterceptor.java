package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private final CustomUserDetailsService customUserDetailsService;

    public TokenAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
}
