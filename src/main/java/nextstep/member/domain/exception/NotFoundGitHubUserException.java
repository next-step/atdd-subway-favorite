package nextstep.member.domain.exception;

public class NotFoundGitHubUserException extends RuntimeException{

    public NotFoundGitHubUserException() {
        super("깃허브에서 사용자를 찾지 못하였습니다");
    }

}
