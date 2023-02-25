package nextstep.member.application.dto.github;

public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse(String email) {
        this.email = email;
    }

    public GithubProfileResponse() {
    }

    public String getEmail() {
        return email;
    }
}
