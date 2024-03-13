package nextstep.member.domain;

import nextstep.auth.application.UserDetails;

import java.util.Objects;

public class LoginMember implements UserDetails {
    private String email;
    private String password;

    public LoginMember(String email) {
        this.email = email;
    }

    public LoginMember(Member member) {
        this.email = member.getEmail();
        this.password = member.getPassword();
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public boolean isEqualPassword(String password) {
        return Objects.equals(this.password, password);
    }
}
