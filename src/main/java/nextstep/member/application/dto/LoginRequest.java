package nextstep.member.application.dto;

public class LoginRequest {
    private String code;

    public LoginRequest() {
    }

    public LoginRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
