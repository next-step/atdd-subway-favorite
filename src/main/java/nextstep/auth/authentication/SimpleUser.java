package nextstep.auth.authentication;

import java.util.List;

public class SimpleUser implements UserDetails {

    private String username;
    private String password;
    private List<String> authorities;

    public SimpleUser() {
    }

    public SimpleUser(String username, String password, List<String> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }
}
