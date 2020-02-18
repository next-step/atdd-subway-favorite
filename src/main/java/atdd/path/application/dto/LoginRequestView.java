package atdd.path.application.dto;

public class LoginRequestView {
    private String email;
    private String password;
    private String accessToken;
    private String tokenType;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
