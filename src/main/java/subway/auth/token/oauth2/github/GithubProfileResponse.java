package subway.auth.token.oauth2.github;

import subway.auth.token.oauth2.OAuth2UserRequest;

public class GithubProfileResponse implements OAuth2UserRequest {

    private String email;
    private Integer age;

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
