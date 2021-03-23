package nextstep.subway.auth.infrastructure.converter;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor.PASSWORD_FIELD;
import static nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor.USERNAME_FIELD;

public class SessionAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }
}
