package nextstep.member.application.response.github;

public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(String email) {
        this.email = email;
    }

    public static GithubProfileResponse from(String email) {
        return new GithubProfileResponse(email);
    }

    public String getEmail() {
        return email;
    }

}
