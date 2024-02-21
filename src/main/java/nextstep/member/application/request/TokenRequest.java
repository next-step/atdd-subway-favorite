package nextstep.member.application.request;

public class TokenRequest {
    private String email;
    private String password;

    private TokenRequest() {
    }

    private TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static TokenRequest of(String email, String password) {
        return new TokenRequest(email, password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
