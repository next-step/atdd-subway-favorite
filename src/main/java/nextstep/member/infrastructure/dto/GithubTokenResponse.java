package nextstep.member.infrastructure.dto;

public class GithubTokenResponse {
    private final String accessToken;

    public GithubTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
