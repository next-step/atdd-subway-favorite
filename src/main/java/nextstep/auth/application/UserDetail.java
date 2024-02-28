package nextstep.auth.application;

public class UserDetail {

    public static UserDetail EMPTY = new UserDetail(null, null);

    protected String email;
    protected String password;

    public UserDetail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isEquals(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
