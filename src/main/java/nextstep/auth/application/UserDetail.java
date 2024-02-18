package nextstep.auth.application;

public class UserDetail {
    private Long id;
    private String email;
    private String password;

    public UserDetail() {
    }

    public UserDetail(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPasswordMismatch(final String password) {
        return !this.password.equals(password);
    }
}
