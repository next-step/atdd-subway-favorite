package nextstep.subway.dto;

public class OauthGithubTokenRequest {
    private String code;

    public OauthGithubTokenRequest() {
    }

    public OauthGithubTokenRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
