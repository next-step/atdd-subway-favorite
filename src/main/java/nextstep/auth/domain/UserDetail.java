package nextstep.auth.domain;

public class UserDetail {
    private String email;
    private String password;

    public UserDetail(String email, String password) {
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
