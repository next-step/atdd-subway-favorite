package nextstep.auth.application.dto;

public class OAuth2LoginRequest {
    private String code;

    public OAuth2LoginRequest() {}

    public OAuth2LoginRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
