package nextstep.member.application.dto;

public class GitHubProfileResponse {

    private String email;

    public GitHubProfileResponse() {
    }

    public GitHubProfileResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
