package nextstep.member.ui.dto;

public class LoginUser {

    private final String email;

    public LoginUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
            "email='" + email + '\'' +
            '}';
    }
}
