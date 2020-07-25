package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationTokenExtractor {
    String USERNAME_FIELD = "username";
    String PASSWORD_FIELD = "password";

    static AuthenticationTokenExtractor of(Type type) {
        try {
            return type.clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("", e);
        }
    }

    AuthenticationToken extract(HttpServletRequest request);

    enum Type {
        FORM_LOGIN(FormLoginExtractor.class),
        BASIC(BasicAuthorizationExtractor.class);

        private final Class<? extends AuthenticationTokenExtractor> clazz;

        Type(Class<? extends AuthenticationTokenExtractor> clazz) {
            this.clazz = clazz;
        }
    }

    class FormLoginExtractor implements AuthenticationTokenExtractor {

        @Override
        public AuthenticationToken extract(HttpServletRequest request) {
            String principal = request.getParameter(USERNAME_FIELD);
            String credentials = request.getParameter(PASSWORD_FIELD);

            return new AuthenticationToken(principal, credentials);
        }
    }

    class BasicAuthorizationExtractor implements AuthenticationTokenExtractor {
        private static final String CREDENTIAL_DELIMITER = ":";
        @Override
        public AuthenticationToken extract(HttpServletRequest request) {
            String token = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
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
}
