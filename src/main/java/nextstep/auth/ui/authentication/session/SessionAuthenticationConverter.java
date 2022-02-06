package nextstep.auth.ui.authentication.session;

import nextstep.auth.domain.AuthenticationToken;
import nextstep.auth.ui.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SessionAuthenticationConverter implements AuthenticationConverter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }
}
