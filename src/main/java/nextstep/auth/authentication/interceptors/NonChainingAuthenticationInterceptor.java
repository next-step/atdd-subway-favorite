package nextstep.auth.authentication.interceptors;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class NonChainingAuthenticationInterceptor implements HandlerInterceptor {

    private final LoginMemberService loginMemberService;

    public NonChainingAuthenticationInterceptor(final LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final AuthenticateRequest authenticateRequest = createLoginRequest(request);
        final LoginMember loginMember = findMember(authenticateRequest.getEmail());

        authenticate(authenticateRequest, loginMember);

        afterAuthenticate(response, loginMember);
        return false;
    }

    abstract AuthenticateRequest createLoginRequest(final HttpServletRequest request) throws IOException;

    private LoginMember findMember(final String email) {
        LoginMember loginMember = loginMemberService.loadUserByUsername(email);

        if (loginMember == null) {
            throw new AuthenticationException();
        }
        return loginMember;
    }

    private void authenticate(final AuthenticateRequest authenticateRequest, final LoginMember loginMember) {
        if (!loginMember.checkPassword(authenticateRequest.getPassword())) {
            throw new AuthenticationException();
        }
    }

    abstract void afterAuthenticate(final HttpServletResponse response, final LoginMember loginMember) throws IOException;

}
