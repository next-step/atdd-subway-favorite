package nextstep.auth.application.dto;

public class GithubAccessTokenRequest {

    private String code;

    private GithubAccessTokenRequest() {
    }

    public GithubAccessTokenRequest(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
