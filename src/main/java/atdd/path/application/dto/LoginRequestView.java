package atdd.path.application.dto;

public class LoginRequestView {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequestView(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
