package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.service.UserDetails;
import nextstep.auth.service.UserDetailsService;

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

        UserDetails loginMember = userDetailsService.loadUserByUsername(email);

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
