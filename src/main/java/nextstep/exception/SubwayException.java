package nextstep.exception;

public class SubwayException extends RuntimeException{
    private final ErrorMessage errorMessage;

    public SubwayException(ErrorMessage errorMessage) {
        super(errorMessage.getDescription());
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
