package nextstep.auth.application;

public class UserDetail {

    public static final UserDetail EMPTY = new UserDetail(null, null);

    protected String email;
    protected String password;

    public UserDetail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public boolean hasUserDetail() {
        return email != null && password != null;
    }

    public String getEmail() {
        return email;
    }

}
