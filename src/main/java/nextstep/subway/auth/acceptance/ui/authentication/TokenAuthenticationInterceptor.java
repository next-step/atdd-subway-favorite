package nextstep.subway.auth.acceptance.ui.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {
    private static final String BASIC_AUTH_REGEX = ":";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        byte[] decodedBytes= Base64.getDecoder().decode(credentials.getBytes());
        String decodedCredentials = new String(decodedBytes);

        String[] split = decodedCredentials.split(BASIC_AUTH_REGEX);

        String principle = split[0];
        String credential = split[1];

        return new AuthenticationToken(principle, credential);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
