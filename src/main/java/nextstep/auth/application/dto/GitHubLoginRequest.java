package nextstep.auth.application.dto;

public class GitHubLoginRequest {

    private String code;

    protected GitHubLoginRequest() {
    }

    public GitHubLoginRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
