package nextstep.member.domain;

public class LoginMember {
    private String email;

    private LoginMember(String email) {
        this.email = email;
    }

    public static LoginMember from(String email) {
        return new LoginMember(email);
    }

    public String getEmail() {
        return email;
    }
}
