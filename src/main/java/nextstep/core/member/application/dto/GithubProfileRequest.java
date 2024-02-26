package nextstep.core.member.application.dto;

public class GithubProfileRequest {
    String accessToken;

    public GithubProfileRequest(String code) {
        this.accessToken = code;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
