package nextstep.auth.application.dto;

public class GithubMemberRequest {
    private final String email;
    private final Integer age;

    public GithubMemberRequest(String email, Integer age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
