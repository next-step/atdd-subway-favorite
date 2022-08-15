package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.UserDetailsService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends Authenticator {

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return new AuthenticationToken(request.getParameter("email"), request.getParameter("password"));
    }

    @Override
    public void authenticate(LoginMember member, HttpServletResponse response) throws IOException {
        Authentication authentication = new Authentication(member.getEmail(), member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
