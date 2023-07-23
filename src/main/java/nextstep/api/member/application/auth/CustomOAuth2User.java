package nextstep.api.member.application.auth;

import nextstep.api.auth.application.token.oauth2.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
    private final String email;
    private final String role;

    public CustomOAuth2User(final String email, final String role) {
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
