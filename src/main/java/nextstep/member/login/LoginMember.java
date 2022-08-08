package nextstep.member.login;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nextstep.auth.user.UserDetails;
import nextstep.member.domain.Member;

@NoArgsConstructor
@EqualsAndHashCode
public class LoginMember implements UserDetails {

    private String email;
    private String password;
    private List<String> authorities;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getEmail(), member.getPassword(), member.getRoles());
    }

    public static LoginMember of(String email, List<String> authorities) {
        return new LoginMember(email, null, authorities);
    }

    public LoginMember(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean isEqualsPassword(String password) {
        return checkPassword(password);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }
}
