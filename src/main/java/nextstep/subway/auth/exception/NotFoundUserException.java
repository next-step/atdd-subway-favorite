package nextstep.subway.auth.exception;

public class NotFoundUserException extends RuntimeException {

    public static String MESSAGE = "존재하지 않는 회원정보입니다.";

    public NotFoundUserException() {
        super(MESSAGE);
    }
}
