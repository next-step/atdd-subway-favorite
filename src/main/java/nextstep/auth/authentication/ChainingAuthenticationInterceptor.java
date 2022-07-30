package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.LoginRequest;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ChainingAuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        try {
            final LoginRequest loginRequest = createLoginRequest(request);
            final LoginMember loginMember = findLoginMember(loginRequest);

            if (!isAuthenticated(loginRequest, loginMember)) {
                throw new AuthenticationException();
            }

            afterAuthenticate(loginMember);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    abstract LoginRequest createLoginRequest(final HttpServletRequest request);

    abstract LoginMember findLoginMember(final LoginRequest loginRequest);

    abstract boolean isAuthenticated(final LoginRequest loginRequest, final LoginMember loginMember);

    private void afterAuthenticate(final LoginMember loginMember) {
        final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
