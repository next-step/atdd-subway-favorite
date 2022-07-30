package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.service.UserDetail;
import nextstep.auth.service.UserDetailsService;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthentication implements AuthenticationStrategy {

    private UserDetailsService userDetailsService;

    public UsernamePasswordAuthentication(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void authenticate(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserDetail userDetail = userDetailsService.loadUserByUsername(email);

        if (userDetail == null) {
            throw new AuthenticationException();
        }

        if (!userDetail.checkPassword(password)) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(userDetail.getEmail(), userDetail.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
