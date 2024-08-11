package nextstep.auth.application.dto;

public class AuthMember {
    private String email;
    private String password;

    public AuthMember() {}

    public AuthMember(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static AuthMember of (String email, String password) {
        return new AuthMember(email, password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
