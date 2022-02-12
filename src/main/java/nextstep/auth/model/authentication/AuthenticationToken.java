package nextstep.auth.model.authentication;

public class AuthenticationToken {
    private String email;
    private String password;

    public AuthenticationToken(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
