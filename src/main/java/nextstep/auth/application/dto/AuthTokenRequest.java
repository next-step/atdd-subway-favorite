package nextstep.auth.application.dto;

public class AuthTokenRequest {
    private String code;

    public String getCode() {
        return code;
    }

    public AuthTokenRequest() {
    }

    public AuthTokenRequest(String code) {
        this.code = code;
    }
}

