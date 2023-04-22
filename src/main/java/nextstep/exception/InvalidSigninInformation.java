package nextstep.exception;

public class InvalidSigninInformation extends AuthException {

    private static final String MESSAGE = "로그인 정보가 올바르지 않습니다.";


    public InvalidSigninInformation(String message) {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

    public InvalidSigninInformation(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }
}
