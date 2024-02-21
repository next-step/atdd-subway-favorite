package nextstep.auth.application.dto;

public class GithubProfileResponse {
    private String email;
    private Integer age;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(final String email, final Integer age) {
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
