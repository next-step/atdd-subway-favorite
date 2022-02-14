package nextstep.auth.authentication.converter;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class SessionAuthenticationConverter implements AuthenticationConverter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String principal = request.getParameterValues(USERNAME_FIELD)[0];
        String credentials = request.getParameterValues(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }
}
