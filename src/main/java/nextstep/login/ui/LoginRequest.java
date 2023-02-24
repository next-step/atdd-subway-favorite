package nextstep.login.ui;

public class LoginRequest {

    private final String email;
    private final String password;

    public LoginRequest(final String email, final String password) {
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
