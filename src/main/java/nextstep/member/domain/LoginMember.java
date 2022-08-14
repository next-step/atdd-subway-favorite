package nextstep.member.domain;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nextstep.auth.userdetail.UserDetails;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
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

    public static LoginMember guest() {
        return new LoginMember();
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPrincipal() {
        return email;
    }

    @Override
    public boolean isInvalidCredentials(String credentials) {
        return !checkPassword(credentials);
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
