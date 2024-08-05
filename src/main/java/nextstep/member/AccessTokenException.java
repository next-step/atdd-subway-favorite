package nextstep.member;

public class AccessTokenException extends RuntimeException {

    public AccessTokenException(MemberErrorMessage memberErrorMessage) {
        super(memberErrorMessage.getMessage());
    }
}
