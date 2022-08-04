package nextstep.auth.interceptors;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationProvidingInterceptor implements HandlerInterceptor {
    protected final LoginMemberService loginMemberService;

    protected AuthenticationProvidingInterceptor(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convert(request);
        LoginMember loginMember = validate(authenticationToken);
        authenticate(loginMember, response);
        return false;
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected LoginMember validate(AuthenticationToken token) {
        LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());
        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return loginMember;
    }

    protected abstract void authenticate(LoginMember loginMember, HttpServletResponse response) throws IOException;
}
