package nextstep.auth.principal;

public class UserPrincipal {

    private final String username;
    private final String role;

    public UserPrincipal(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
