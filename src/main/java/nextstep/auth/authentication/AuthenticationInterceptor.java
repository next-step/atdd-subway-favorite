package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    protected abstract boolean proceed();

}
