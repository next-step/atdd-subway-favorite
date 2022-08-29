package nextstep.auth.authentication;

public class UserInformation {
    private final String email;
    private final String password;

    public UserInformation(String email, String password) {
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
