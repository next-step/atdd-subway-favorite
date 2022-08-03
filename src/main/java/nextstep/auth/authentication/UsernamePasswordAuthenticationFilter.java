package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends LoginFilter {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        super(loginMemberService);
    }

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response) {
        final String principal = request.getParameter(USERNAME_FIELD);
        final String credentials = request.getParameter(PASSWORD_FIELD);

        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (loginMember.isInvalidPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
