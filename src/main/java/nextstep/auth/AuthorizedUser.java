package nextstep.auth;

import java.util.List;

public class AuthorizedUser {

    private String email;
    private List<String> roles;

    public AuthorizedUser(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}