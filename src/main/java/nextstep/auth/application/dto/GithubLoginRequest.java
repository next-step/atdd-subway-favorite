package nextstep.auth.application.dto;

public class GithubLoginRequest {

    private String code;

    public GithubLoginRequest() {
    }

    public GithubLoginRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
