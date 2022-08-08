package nextstep.member.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nextstep.auth.UserDetails;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

}
