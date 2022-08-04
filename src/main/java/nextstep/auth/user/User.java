package nextstep.auth.user;

import java.util.List;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.domain.Member;

public class User implements UserDetails {

    private String email;
    private String password;
    private List<String> authorities;

    public static User of(Member member) {
        return new User(member.getEmail(), member.getPassword(), member.getRoles());
    }

    public static User of(String email, List<String> authorities) {
        return new User(email, null, authorities);
    }

    public static User guest() {
        return new User();
    }

    private User() {
    }

    public User(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new AuthenticationException();
        }
        return true;
    }

}
