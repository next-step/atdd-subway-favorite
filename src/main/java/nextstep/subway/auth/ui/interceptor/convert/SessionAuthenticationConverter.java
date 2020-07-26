package nextstep.subway.auth.ui.interceptor.convert;

import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.AuthenticationToken;

@Component
public class SessionAuthenticationConverter implements AuthenticationConverter {

    public static final String PRINCIPAL = "username";
    public static final String CREDENTIALS = "password";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        validatePrincipalAndCredentials(parameterMap);
        String principal = parameterMap.get(PRINCIPAL)[0];
        String credentials = parameterMap.get(CREDENTIALS)[0];

        return new AuthenticationToken(principal, credentials);
    }

    private void validatePrincipalAndCredentials(Map<String, String[]> parameterMap) {
        if (Objects.isNull(parameterMap.get(PRINCIPAL)) || Objects.isNull(parameterMap.get(CREDENTIALS))) {
            throw new RuntimeException("no principal or credentials found.");
        }
    }
}
