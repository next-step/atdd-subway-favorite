package nextstep.member.application.exception;

public class UserNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "유저를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
