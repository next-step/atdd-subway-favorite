package nextstep.member.application;

public class WrongPasswordException extends RuntimeException {
    private final static String MESSAGE = "틀린 암호입니다. (요청값: %s)";
    public WrongPasswordException(String password) {
        super(String.format(MESSAGE, password));
    }
}
