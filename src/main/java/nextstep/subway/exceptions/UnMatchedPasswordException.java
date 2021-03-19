package nextstep.subway.exceptions;

public class UnMatchedPasswordException extends RuntimeException {

    public static final String DEFAULT_MSG = "비밀번호가 일치하지 않습니다.";

    public UnMatchedPasswordException() {
        super(DEFAULT_MSG);
    }

    public UnMatchedPasswordException(String msg) {
        super(msg);
    }
}
