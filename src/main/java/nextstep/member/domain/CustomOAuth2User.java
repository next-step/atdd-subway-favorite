package nextstep.member.domain;

import nextstep.auth.token.oauth2.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
    private String email;
    private String role;

    public CustomOAuth2User(String email, String role) {
        this.email = email;
        this.role = role;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getRole() {
        return role;
    }
}
