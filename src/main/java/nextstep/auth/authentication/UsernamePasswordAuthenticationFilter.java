package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.LoginMemberService;

import javax.servlet.http.HttpServletRequest;

public class UsernamePasswordAuthenticationFilter extends AuthenticationProcessingFilter {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private final LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) {
        var username = request.getParameter(USERNAME_FIELD);
        var password = request.getParameter(PASSWORD_FIELD);

        return new AuthenticationToken(username, password);
    }

    @Override
    protected Authentication authenticate(AuthenticationToken authenticationToken) {
        var loginMember = loginMemberService.loadUserByUsername(authenticationToken.getPrincipal());

        if (!loginMember.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }
}
