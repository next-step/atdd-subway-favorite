package subway.member.domain;

import lombok.Builder;
import subway.auth.token.oauth2.OAuth2User;

@Builder
public class CustomOAuth2User implements OAuth2User {
    private String email;
    private String role;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getRole() {
        return role;
    }

    public static CustomOAuth2User from (Member member) {
        return CustomOAuth2User.builder()
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }
}
