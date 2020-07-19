package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationInterceptor implements HandlerInterceptor {

    private static final String CREDENTIAL_DELIMITER = ":";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        return false;
    }

    public AuthenticationToken convertToken(String token) {
        String decodedToken = new String(Base64.decodeBase64(token.getBytes()));
        int delimiterIndex = decodedToken.indexOf(CREDENTIAL_DELIMITER);

        if (delimiterIndex < 0) {
            throw new IllegalArgumentException("invalid token string");
        }

        String principal = decodedToken.substring(0, delimiterIndex);
        String credentials = decodedToken.substring(delimiterIndex + 1);

        return new AuthenticationToken(principal, credentials);
    }
}
