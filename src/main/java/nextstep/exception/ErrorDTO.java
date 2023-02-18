package nextstep.exception;

public class ErrorDTO {
    private final String title;

    private final String message;

    public ErrorDTO(ErrorMessage errorMessage) {
        this.title = errorMessage.name();
        this.message = errorMessage.getDescription();
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
