package nextstep.line.domain.exception;

public class CantDeleteSectionException extends IllegalSectionOperationException {
    public CantDeleteSectionException(String message) {
        super(message);
    }
}
