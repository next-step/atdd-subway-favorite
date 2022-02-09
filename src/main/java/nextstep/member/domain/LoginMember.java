package nextstep.member.domain;

import lombok.Getter;
import nextstep.auth.domain.UserDetails;

@Getter
public class LoginMember implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer age;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public LoginMember(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
