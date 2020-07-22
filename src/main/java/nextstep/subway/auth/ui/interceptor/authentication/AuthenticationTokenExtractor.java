package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        ;

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
}
