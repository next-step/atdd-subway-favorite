package nextstep.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.member.domain.Member;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserDetails {
    private final String username;
    private final String password;
    private final List<String> authorities;

    public static UserDetails from(Member member) {
        return new UserDetails(member.getEmail(), member.getPassword(), member.getRoles());
    }


    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}
