package subway.member.domain;

import lombok.Builder;
import subway.auth.token.oauth2.OAuth2User;

@Builder
public class CustomOAuth2User implements OAuth2User {
    private String email;
    private RoleType role;

    public static CustomOAuth2User from(Member member) {
        return CustomOAuth2User.builder()
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public RoleType getRole() {
        return role;
    }
}
