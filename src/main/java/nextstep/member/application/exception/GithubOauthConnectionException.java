package nextstep.member.application.exception;

public class GithubOauthConnectionException extends NotFoundMemberException {

    public GithubOauthConnectionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
