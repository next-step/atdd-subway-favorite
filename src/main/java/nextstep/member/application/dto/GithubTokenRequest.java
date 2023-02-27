package nextstep.member.application.dto;

public class GithubTokenRequest {
    private String code;

    private GithubTokenRequest() {
    }

    public GithubTokenRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
