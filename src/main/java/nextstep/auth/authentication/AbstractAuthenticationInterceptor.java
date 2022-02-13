package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationConverter converter;
    private final ProviderManager providerManager;

    public AbstractAuthenticationInterceptor(AuthenticationConverter converter, ProviderManager providerManager) {
        this.converter = converter;
        this.providerManager = providerManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = converter.convert(request);
        Authentication authentication = providerManager.authenticate(token);

        afterAuthentication(request, response, authentication);
        return false;
    }

    protected abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
