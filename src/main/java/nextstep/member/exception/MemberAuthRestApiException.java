package nextstep.member.exception;

public class MemberAuthRestApiException extends RuntimeException{
    public MemberAuthRestApiException() {
    }

    public MemberAuthRestApiException(String message) {
        super(message);
    }
}
