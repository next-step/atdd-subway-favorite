package nextstep.line.exception;


import nextstep.common.exception.ValidationError;

public class CreateRequestNotValidException extends ValidationError {
    public CreateRequestNotValidException(final String message) {
        super(message);
    }
}
