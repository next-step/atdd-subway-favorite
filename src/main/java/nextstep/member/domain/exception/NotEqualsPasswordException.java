package nextstep.member.domain.exception;

public class NotEqualsPasswordException extends RuntimeException {

    public NotEqualsPasswordException() {
        super("비밀번호가 다릅니다.");
    }

}
