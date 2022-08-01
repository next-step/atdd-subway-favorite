package nextstep.auth.exception;

public class BuildResponseTokenFailException extends RuntimeException {

    public BuildResponseTokenFailException() {
        super("해당 데이터를 Token으로 만들 수 없습니다");
    }
}
