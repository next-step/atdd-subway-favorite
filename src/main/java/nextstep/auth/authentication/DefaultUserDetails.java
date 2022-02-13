package nextstep.auth.authentication;

public class DefaultUserDetails implements UserDetails {
    private final String username;
    private final String password;

    public DefaultUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
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
    public boolean checkPassword(String credentials) {
        return this.password.equals(credentials);
    }
}
