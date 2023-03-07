package nextstep.member.application.dto;

public class JwtTokenResponse {
    private String accessToken;

    public JwtTokenResponse() {
    }

    public JwtTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
