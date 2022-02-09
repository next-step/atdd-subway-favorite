package nextstep.auth.authentication.session;

import nextstep.auth.authentication.AbstractAuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.ProviderManager;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

    public SessionAuthenticationInterceptor(AuthenticationConverter converter, ProviderManager providerManager) {
        super(converter, providerManager);
    }

    @Override
    protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
