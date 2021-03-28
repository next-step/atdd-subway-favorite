package nextstep.subway.exception;

public class UserDetailsNotExistException extends BusinessException{

    private static final String MESSAGE = "UserDetails이 존재하지 않습니다";

    public UserDetailsNotExistException() {
        super(MESSAGE);
    }
}
