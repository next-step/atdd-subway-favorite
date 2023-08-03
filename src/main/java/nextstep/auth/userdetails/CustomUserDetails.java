package nextstep.auth.userdetails;

import lombok.Builder;
import lombok.Getter;
import nextstep.member.entity.Member;

import java.util.Objects;

@Builder
public class CustomUserDetails implements UserDetails {

    @Getter
    private String email;

    private String password;

    private String role;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public static CustomUserDetails of(Member member) {
        return CustomUserDetails.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }

}
