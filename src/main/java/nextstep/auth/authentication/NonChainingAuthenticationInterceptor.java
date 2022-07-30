package nextstep.auth.authentication;

import nextstep.auth.token.LoginRequest;
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
        final LoginRequest loginRequest = createLoginRequest(request);
        final LoginMember loginMember = findMember(loginRequest.getEmail());

        authenticate(loginRequest, loginMember);

        afterAuthenticate(response, loginMember);
        return false;
    }

    abstract LoginRequest createLoginRequest(final HttpServletRequest request) throws IOException;

    private LoginMember findMember(final String email) {
        LoginMember loginMember = loginMemberService.loadUserByUsername(email);

        if (loginMember == null) {
            throw new AuthenticationException();
        }
        return loginMember;
    }

    private void authenticate(final LoginRequest loginRequest, final LoginMember loginMember) {
        if (!loginMember.checkPassword(loginRequest.getPassword())) {
            throw new AuthenticationException();
        }
    }

    abstract void afterAuthenticate(final HttpServletResponse response, final LoginMember loginMember) throws IOException;

}
