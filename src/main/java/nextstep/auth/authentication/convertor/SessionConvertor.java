package nextstep.auth.authentication.convertor;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class SessionConvertor implements AuthenticationConverter {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String principal = request.getParameter(USERNAME_FIELD);
        String credentials = request.getParameter(PASSWORD_FIELD);

        return new AuthenticationToken(principal, credentials);
    }
}
