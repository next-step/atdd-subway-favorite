package nextstep.common.exception;

public final class CannotInstanceException extends RuntimeException {

    private static final String MESSAGE = "해당 클래스는 인스턴스화 할 수 없습니다.";

    public CannotInstanceException() {
        super(MESSAGE);
    }
}
