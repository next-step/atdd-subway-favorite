package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.LoginRequest;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationInterceptorNon extends NonChainingAuthenticationInterceptor {

    public UsernamePasswordAuthenticationInterceptorNon(LoginMemberService loginMemberService) {
        super(loginMemberService);
    }

    LoginRequest createLoginRequest(final HttpServletRequest request) {
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");

        return new LoginRequest(email, password);
    }

    void afterAuthenticate(final HttpServletResponse response, final LoginMember loginMember) {
        final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
