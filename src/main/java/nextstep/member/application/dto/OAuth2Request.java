package nextstep.member.application.dto;

public class OAuth2Request {

    private String code;

    public OAuth2Request(String code) {
        this.code = code;
    }

    public OAuth2Request() {
    }

    public String getCode() {
        return code;
    }
}
