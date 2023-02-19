package nextstep.member.application.exception;

public class GithubOauthConnectionException extends MemberException {

    public GithubOauthConnectionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
