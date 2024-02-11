package nextstep.path.exception;


import nextstep.common.exception.ValidationError;

public class PathSearchNotValidException extends ValidationError {
    public PathSearchNotValidException(final String message) {
        super(message);
    }
}
