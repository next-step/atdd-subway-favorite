package nextstep.subway.exceptions;

public class NotFoundUserException extends RuntimeException {

    public static final String DEFAULT_MSG = "해당 이름의 회원을 찾을 수 없습니다.";

    public NotFoundUserException() {
        super(DEFAULT_MSG);
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
