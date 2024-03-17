package nextstep.member.application.dto;

public class GitHubProfileResponse {

    private final String email;
    private final int age;

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
