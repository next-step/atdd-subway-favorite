package nextstep.member.application.dto;

public class TokenRequest{
    private String email;
    private String password;

    public TokenRequest() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
