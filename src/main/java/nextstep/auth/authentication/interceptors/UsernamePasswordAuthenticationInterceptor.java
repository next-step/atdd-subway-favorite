package nextstep.auth.authentication.interceptors;

import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationInterceptor extends NonChainingAuthenticationInterceptor {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    public UsernamePasswordAuthenticationInterceptor(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    AuthenticateRequest createLoginRequest(final HttpServletRequest request) {
        final String email = request.getParameter(EMAIL);
        final String password = request.getParameter(PASSWORD);

        return new AuthenticateRequest(email, password);
    }

    void afterAuthenticate(final HttpServletResponse response, final UserDetails loginMember) {
        final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
