package nextstep.auth.config;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class AuthorizationExtractor {

    private static final String AUTHORIZATION = "Authorization";
    private static String BEARER_TYPE = "Bearer";

    public static String extract(final HttpServletRequest request, final String type) {
        final Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            final String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }
}
