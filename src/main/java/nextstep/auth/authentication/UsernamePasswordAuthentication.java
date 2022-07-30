package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthentication implements AuthenticationStrategy {

    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthentication(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public void authenticate(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        LoginMember loginMember = loginMemberService.loadUserByUsername(email);

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(password)) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
