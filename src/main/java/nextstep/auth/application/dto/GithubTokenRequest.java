package nextstep.auth.application.dto;

public class GithubTokenRequest {

    private String code;

    private GithubTokenRequest() {
    }

    public GithubTokenRequest(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
