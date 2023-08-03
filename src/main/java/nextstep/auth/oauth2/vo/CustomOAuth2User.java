package nextstep.auth.oauth2.vo;

import lombok.Builder;
import lombok.Getter;
import nextstep.auth.oauth2.dto.OAuth2User;
import nextstep.member.entity.Member;

@Builder
public class CustomOAuth2User implements OAuth2User {

    @Getter
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
