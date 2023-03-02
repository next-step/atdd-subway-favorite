package nextstep.member.application.dto.github;

public class GithubProfileResponse {
    private final String code;
    private final String accessToken;
    private final String email;

    public GithubProfileResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }
}
