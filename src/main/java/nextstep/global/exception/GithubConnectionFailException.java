package nextstep.global.exception;

public class GithubConnectionFailException extends RuntimeException {

    private static final String MESSAGE = "Github 응답이 없습니다";

    public GithubConnectionFailException() {
        super(MESSAGE);
    }
}
