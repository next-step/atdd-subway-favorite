package nextstep.auth.config;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class AuthorizationExtractor {

    private static final String AUTHORIZATION = "Authorization";
    private static String BEARER_TYPE = "Bearer";
    private static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    public static String extract(final HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                request.setAttribute(ACCESS_TOKEN_TYPE, value.substring(0, BEARER_TYPE.length()).trim());
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return Strings.EMPTY;
    }
}
