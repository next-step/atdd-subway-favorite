package nextstep.member.application.exception;

import nextstep.common.exception.ErrorCode;

public class GithubOauthConnectionException extends NotFoundMemberException {

    public GithubOauthConnectionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
