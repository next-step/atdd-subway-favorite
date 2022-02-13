package nextstep.auth.authentication.converter;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class SessionAuthenticationConverter implements AuthenticationConverter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String principal = request.getParameter(USERNAME_FIELD);
        String credentials = request.getParameter(PASSWORD_FIELD);

        return new AuthenticationToken(principal, credentials);
    }
}
