package atdd.path.application.dto;

import java.util.StringJoiner;

public class LoginRequestView {

    private String email;
    private String password;

    protected LoginRequestView() {}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginRequestView.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .toString();
    }

}
