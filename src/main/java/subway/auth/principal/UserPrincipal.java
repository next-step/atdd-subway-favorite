package subway.auth.principal;

public class UserPrincipal {
    private String username;
    private String role;

    public UserPrincipal(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
