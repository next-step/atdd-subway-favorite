package nextstep.auth.application.dto;

public class GithubProfileResponse {
    private String email;

    protected GithubProfileResponse() {
    }

    public GithubProfileResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
