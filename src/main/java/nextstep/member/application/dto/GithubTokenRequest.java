package nextstep.member.application.dto;

public class GithubTokenRequest {
    private String code;

    public GithubTokenRequest() {
    }

    public GithubTokenRequest(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
