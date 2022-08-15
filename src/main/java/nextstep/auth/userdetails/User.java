package nextstep.auth.userdetails;

import java.util.List;

public class User implements UserDetails {
    private final String username;
    private final String password;
    private final List<String> authorities;

    public User(String username, String password, List<String> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Object getUsername() {
        return username;
    }

    @Override
    public Object getPassword() {
        return password;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return password.equals(credentials.toString());
    }
}
