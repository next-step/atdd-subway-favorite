package nextstep.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import nextstep.auth.userdetails.UserDetails;

@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

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
        return this.password.equals(password);
    }

    public static CustomUserDetails of(Member member) {
        return CustomUserDetails.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }

}
