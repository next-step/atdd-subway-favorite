package nextstep.line.domain.exception;

public class CantAddSectionException extends IllegalSectionOperationException {
    public CantAddSectionException(String message) {
        super(message);
    }
}
