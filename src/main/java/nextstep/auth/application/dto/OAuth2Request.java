package nextstep.auth.application.dto;

public class OAuth2Request {
    private String code;

    public OAuth2Request() {
    }

    public OAuth2Request(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
