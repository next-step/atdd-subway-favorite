package nextstep.subway.member.exception;

public class MemberNonExistException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE_NON_EXIST_MEMBER = "존재하지 않는 사용자입니다. 다시 확인해주세요.";

    public MemberNonExistException() {
        super(EXCEPTION_MESSAGE_NON_EXIST_MEMBER);
    }
}
