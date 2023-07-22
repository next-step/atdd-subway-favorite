package nextstep.api.auth.application.token.oauth2.github.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.api.auth.application.token.oauth2.OAuth2UserRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubProfileResponse implements OAuth2UserRequest {
    private String email;
    private Integer age;

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
