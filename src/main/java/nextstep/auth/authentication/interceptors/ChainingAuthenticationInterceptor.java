package nextstep.auth.authentication.interceptors;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ChainingAuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        try {
            final AuthenticateRequest authenticateRequest = createLoginRequest(request);
            final LoginMember loginMember = findLoginMember(authenticateRequest);

            if (!isAuthenticated(authenticateRequest, loginMember)) {
                throw new AuthenticationException();
            }

            afterAuthenticate(loginMember);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    abstract AuthenticateRequest createLoginRequest(final HttpServletRequest request);

    abstract LoginMember findLoginMember(final AuthenticateRequest authenticateRequest);

    abstract boolean isAuthenticated(final AuthenticateRequest authenticateRequest, final LoginMember loginMember);

    private void afterAuthenticate(final LoginMember loginMember) {
        final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
