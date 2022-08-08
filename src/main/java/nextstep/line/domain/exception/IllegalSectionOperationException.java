package nextstep.line.domain.exception;

public abstract class IllegalSectionOperationException extends RuntimeException {
    IllegalSectionOperationException(String message) {
        super(message);
    }
}
