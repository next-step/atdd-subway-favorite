package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
