package nextstep.subway.exception;

public class UnAuthorizedException extends BusinessException{

    private static final String MESSAGE = "인증되지 않은 사용자입니다.";

    public UnAuthorizedException() {
        super(MESSAGE);
    }
}
