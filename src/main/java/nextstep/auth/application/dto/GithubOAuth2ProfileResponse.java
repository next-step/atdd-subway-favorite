package nextstep.auth.application.dto;

public class GithubOAuth2ProfileResponse {

    private String email;
    private Integer age;

    public GithubOAuth2ProfileResponse() {
    }

    public GithubOAuth2ProfileResponse(final String email, final Integer age) {
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
