package nextstep.auth.application.dto;

public class ApplicationTokenResponse {
    private String accessToken;

    public ApplicationTokenResponse() {
    }

    public ApplicationTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
