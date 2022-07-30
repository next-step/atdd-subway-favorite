package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.NotFoundMemberException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationNonChainingFilter implements HandlerInterceptor {
    private final LoginMemberService loginMemberService;

    public AuthenticationNonChainingFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthenticationToken token = getAuthenticationToken(request);
        Authentication authentication = getAuthentication(token);
        afterAuthentication(authentication, response);
        return false;
    }

    protected AuthenticationToken createToken(String username, String password) {
        return new AuthenticationToken(username, password);
    }

    private Authentication getAuthentication(AuthenticationToken token) {
        LoginMember loginMember = findLoginMember(token);
        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }

    private LoginMember findLoginMember(AuthenticationToken token) {
        try {
            LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());

            if (loginMember.invalidPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            return loginMember;
        } catch (NotFoundMemberException e) {
            throw new AuthenticationException();
        }
    }

    protected abstract AuthenticationToken getAuthenticationToken(HttpServletRequest request);

    protected abstract void afterAuthentication(Authentication authentication, HttpServletResponse response);
}
