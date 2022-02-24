package nextstep.global.error.exception;

public class NotFoundMemberException extends RuntimeException {
    private static final String MESSAGE = "Member 를 찾을 수 없습니다.";

    public NotFoundMemberException() {
    }
}
