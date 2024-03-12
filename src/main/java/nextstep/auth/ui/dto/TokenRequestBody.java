package nextstep.auth.ui.dto;

public class TokenRequestBody {
    private String email;
    private String password;

    protected TokenRequestBody() {
    }

    public TokenRequestBody(String email, String password) {
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
