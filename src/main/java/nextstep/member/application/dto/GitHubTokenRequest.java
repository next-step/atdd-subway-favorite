package nextstep.member.application.dto;

public class GitHubTokenRequest {

    private String code;

    public GitHubTokenRequest() {
    }

    public GitHubTokenRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
