package nextstep.auth.member;

import java.util.List;

public class LoginMember implements UserDetails {
    private String email;
    private String password;
    private List<String> authorities;

    public LoginMember() {
    }

    public LoginMember(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public LoginMember(String email, List<String> authorities) {
        this.email = email;
        this.authorities = authorities;
    }

    public static LoginMember of(String email, String password, List<String> authorities) {
        return new LoginMember(email, password, authorities);
    }

    public static LoginMember of(String username, List<String> authorities) {
        return new LoginMember(username, authorities);
    }

    public static LoginMember guest() {
        return new LoginMember();
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean checkPassword(String credentials) {
        return this.password.equals(credentials);
    }
}
