package nextstep.auth.principal;

public class UserPrincipal {
    private String username;
    private String role;

    public UserPrincipal(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUserMail() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
