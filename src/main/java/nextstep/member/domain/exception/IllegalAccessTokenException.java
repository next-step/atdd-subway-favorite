package nextstep.member.domain.exception;

public class IllegalAccessTokenException extends RuntimeException {

    public IllegalAccessTokenException() {
        super("잘못된 AccessToken 입니다.");
    }

}
