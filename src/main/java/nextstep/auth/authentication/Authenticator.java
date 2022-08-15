package nextstep.auth.authentication;

import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Authenticator implements HandlerInterceptor {

    private final LoginMemberService loginMemberService;

    public Authenticator(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = convert(request);
        LoginMember member;

        try {
            member = loginMemberService.loadUserByUsername(token.getPrincipal());
        } catch (RuntimeException e) {
            throw new AuthenticationException();
        }

        checkAuthentication(member, token.getCredentials());
        authenticate(member, response);

        return false;
    }

    abstract public AuthenticationToken convert(HttpServletRequest request) throws IOException;

    abstract public void authenticate(LoginMember member, HttpServletResponse response) throws IOException;

    private void checkAuthentication(LoginMember member, String password) {
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
