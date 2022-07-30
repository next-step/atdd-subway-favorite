package nextstep.auth.authentication.interceptors;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationInterceptor extends NonChainingAuthenticationInterceptor {

    public UsernamePasswordAuthenticationInterceptor(LoginMemberService loginMemberService) {
        super(loginMemberService);
    }

    AuthenticateRequest createLoginRequest(final HttpServletRequest request) {
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");

        return new AuthenticateRequest(email, password);
    }

    void afterAuthenticate(final HttpServletResponse response, final LoginMember loginMember) {
        final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
