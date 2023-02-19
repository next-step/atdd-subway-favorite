package nextstep.auth.application.dto;

public class GithubProfileResponse {
    private String email;

    private GithubProfileResponse() {}

    public GithubProfileResponse(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
