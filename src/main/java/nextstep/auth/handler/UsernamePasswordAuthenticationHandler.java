package nextstep.auth.handler;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UsernamePasswordAuthenticationHandler extends AuthenticationHandler {


    public UsernamePasswordAuthenticationHandler(LoginMemberService loginMemberService) {
        super(loginMemberService);
    }

    @Override
    protected LoginMember preAuthentication(HttpServletRequest request) {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        return findLoginMember(username, password);
    }

    @Override
    protected void afterAuthentication(LoginMember loginMember, HttpServletResponse response) throws IOException {
        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
