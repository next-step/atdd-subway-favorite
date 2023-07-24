package subway.member.domain;

import lombok.Builder;
import subway.auth.userdetails.UserDetails;

@Builder
public class CustomUserDetails implements UserDetails {
    private String email;
    private String password;
    private RoleType role;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public RoleType getRole() {
        return role;
    }

    public static CustomUserDetails from(Member member) {
        return CustomUserDetails.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
