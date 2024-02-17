package nextstep.auth.domain;

import lombok.Getter;

@Getter
public class OAuth2UserRequest {
    String username;

    int age;

    public OAuth2User toOAuth2User() {
        return new OAuth2User(username, "", age);
    }
}
