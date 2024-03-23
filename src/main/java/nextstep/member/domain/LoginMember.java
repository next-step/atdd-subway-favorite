package nextstep.member.domain;

public class LoginMember {
    private final String email;

    public LoginMember(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
