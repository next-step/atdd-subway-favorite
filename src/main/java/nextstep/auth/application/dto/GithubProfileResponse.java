package nextstep.auth.application.dto;

public class GithubProfileResponse {
    private Long id;
    private String email;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
