package nextstep.auth.application.dto;

public class AuthResponse {

    private String accessToken;

    public AuthResponse() {
    }

    public AuthResponse(final String accessToken) {
        this.accessToken = accessToken;
    }


    public String getAccessToken() {
        return accessToken;
    }
}
