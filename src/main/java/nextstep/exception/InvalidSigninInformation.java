package nextstep.exception;

import org.springframework.http.HttpStatus;

public class InvalidSigninInformation extends AuthException {

    private static final String MESSAGE = "로그인 정보가 올바르지 않습니다.";


    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    public InvalidSigninInformation(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }
}
