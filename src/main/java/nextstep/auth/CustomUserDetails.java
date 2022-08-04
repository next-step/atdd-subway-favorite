package nextstep.auth;

import lombok.AllArgsConstructor;
import nextstep.member.domain.Member;

import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails{

    private String username;
    private String password;
    private List<String> authorities;

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public List<String> getAuthorities() {
        return this.authorities;
    }

    public static CustomUserDetails from(Member member) {
        return new CustomUserDetails(member.getEmail(), member.getPassword(), member.getRoles());
    }
}
