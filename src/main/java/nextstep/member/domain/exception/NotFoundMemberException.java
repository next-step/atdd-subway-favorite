package nextstep.member.domain.exception;

public class NotFoundMemberException extends RuntimeException {

    public NotFoundMemberException() {
        super("사용자를 못찾았습니다");
    }

}
