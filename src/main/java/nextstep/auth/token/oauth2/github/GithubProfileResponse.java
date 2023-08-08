package nextstep.auth.token.oauth2.github;

import nextstep.auth.token.oauth2.OAuth2UserRequest;

public class GithubProfileResponse implements OAuth2UserRequest {

    private String email;
    private Integer age;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(String email, Integer age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Integer getAge() {
        return age;
    }
}
