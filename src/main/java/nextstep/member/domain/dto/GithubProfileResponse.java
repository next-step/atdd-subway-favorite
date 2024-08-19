package nextstep.member.domain.dto;

public class GithubProfileResponse {

    private String email;

    public GithubProfileResponse() {}

    public GithubProfileResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
