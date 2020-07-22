package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationTokenExtractor {
    String USERNAME_PARAMETER_NAME = "username";
    String PASSWORD_PARAMTER_NAME = "password";

    static AuthenticationTokenExtractor of(Type type) {
        return null;
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
            return null;
        }
    }
}
