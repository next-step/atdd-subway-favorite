package nextstep.member.application.dto.github;

public class GithubProfileResponse {

    private String code;
    private String accessToken;
    private String email;

    private GithubProfileResponse() {
    }

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
