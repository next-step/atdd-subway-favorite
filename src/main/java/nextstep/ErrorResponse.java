package nextstep;

import java.util.List;

public class ErrorResponse {

    private List<String> messages;

    public ErrorResponse(final List<String> messages) {
        super();
        this.messages = messages;
    }

    public static ErrorResponse from(final String message) {
        return new ErrorResponse(List.of(message));
    }

    public List<String> getMessage() {
        return messages;
    }
}
