package nextstep.line.exception;


import nextstep.common.exception.ValidationError;

public class UpdateRequestNotValidException extends ValidationError {
    public UpdateRequestNotValidException(final String message) {
        super(message);
    }
}
