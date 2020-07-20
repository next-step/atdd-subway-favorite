package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class FormAuthenticationConverter implements AuthenticationConverter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        return new AuthenticationToken(request.getParameter(USERNAME_FIELD), request.getParameter(PASSWORD_FIELD));
    }
}
