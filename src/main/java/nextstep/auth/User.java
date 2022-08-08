package nextstep.auth;

import lombok.AllArgsConstructor;
import nextstep.member.domain.Member;

import java.util.List;

@AllArgsConstructor
public class User implements UserDetails{

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

    public static User from(Member member) {
        return new User(member.getEmail(), member.getPassword(), member.getRoles());
    }
}
