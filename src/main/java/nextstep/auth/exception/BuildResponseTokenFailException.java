package nextstep.auth.exception;

public class BuildResponseTokenFailException extends RuntimeException {

    public static final String MESSAGE = "해당 데이터를 Token으로 만들 수 없습니다";

    public BuildResponseTokenFailException() {
        super(MESSAGE);
    }
}
