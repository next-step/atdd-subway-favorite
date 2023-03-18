package nextstep.member.application.dto;

public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
