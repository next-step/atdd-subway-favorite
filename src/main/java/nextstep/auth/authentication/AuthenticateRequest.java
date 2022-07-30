package nextstep.auth.authentication;

public class AuthenticateRequest {
    private String email;
    private String password;

    public AuthenticateRequest() {
    }

    public AuthenticateRequest(String email, String password) {
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
