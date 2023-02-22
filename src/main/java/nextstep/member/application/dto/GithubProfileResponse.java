package nextstep.member.application.dto;

public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse(final String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return email;
    }
}
