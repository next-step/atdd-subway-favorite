package nextstep;

import lombok.Getter;

@Getter
public class FailResponse {
    private String message;

    public FailResponse(String message) {
        this.message = message;
    }
}
