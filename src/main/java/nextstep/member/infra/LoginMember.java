package nextstep.member.infra;

import nextstep.auth.user.UserDetails;
import nextstep.member.domain.Member;

import java.util.List;

public class LoginMember implements UserDetails {
    private final String email;
    private final String password;
    private final List<String> roles;

    public LoginMember(Member member) {
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.roles = member.getRoles();
    }

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getPrincipal() {
        return email;
    }

    @Override
    public List<String> getAuthorities() {
        return roles;
    }
}
