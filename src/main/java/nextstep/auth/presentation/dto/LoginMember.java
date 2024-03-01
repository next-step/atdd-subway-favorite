package nextstep.auth.presentation.dto;

public class LoginMember {
    private String email;

    public LoginMember(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
