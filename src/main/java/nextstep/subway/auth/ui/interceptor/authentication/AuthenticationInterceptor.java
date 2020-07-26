package nextstep.subway.auth.ui.interceptor.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.subway.auth.domain.Authentication;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException;
}
