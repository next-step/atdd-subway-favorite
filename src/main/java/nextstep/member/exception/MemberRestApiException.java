package nextstep.member.exception;

public class MemberRestApiException extends RuntimeException{
    public MemberRestApiException() {
    }

    public MemberRestApiException(String message) {
        super(message);
    }
}
