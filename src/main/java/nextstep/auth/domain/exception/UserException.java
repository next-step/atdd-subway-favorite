package nextstep.auth.domain.exception;

import nextstep.exception.DomainException;

public class UserException extends DomainException {

    public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";

    public UserException(String message) {
        super(message);
    }

    public static class NotFoundUserException extends UserException {

        public NotFoundUserException() {
            super(USER_NOT_FOUND);
        }
    }
}
