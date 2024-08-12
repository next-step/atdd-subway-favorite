package nextstep.security.oauth2;

public class GithubUserInfoResponse {

    private String email;

    private String name;

    public GithubUserInfoResponse() {
    }

    public GithubUserInfoResponse(final String email, final String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
