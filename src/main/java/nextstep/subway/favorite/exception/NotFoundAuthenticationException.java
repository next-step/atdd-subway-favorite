package nextstep.subway.favorite.exception;

import javax.naming.AuthenticationException;

public class NotFoundAuthenticationException extends AuthenticationException {
    NotFoundAuthenticationException() {
        super("유효하지 않은 인증 정보입니다. ");
    }
}
