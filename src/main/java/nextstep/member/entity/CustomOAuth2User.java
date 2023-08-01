package nextstep.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import nextstep.auth.oauth2.OAuth2User;

@Builder
@AllArgsConstructor
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

    public static CustomOAuth2User of(Member member) {
        return CustomOAuth2User.builder()
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }

}
