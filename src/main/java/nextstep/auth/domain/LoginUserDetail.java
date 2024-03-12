package nextstep.auth.domain;

public class LoginUserDetail {
    private final String email;

    public LoginUserDetail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
