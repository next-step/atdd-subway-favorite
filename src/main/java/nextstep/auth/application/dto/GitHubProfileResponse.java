package nextstep.auth.application.dto;

public class GitHubProfileResponse {

    private String email;
    private int age;

    public GitHubProfileResponse() {
    }

    public GitHubProfileResponse(String email, int age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
