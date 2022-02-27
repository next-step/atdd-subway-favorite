package nextstep.member.domain;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.domain.UserDetails;

public class AuthenticationMember implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer age;

    public static AuthenticationMember of(Member member) {
        return new AuthenticationMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public AuthenticationMember(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    @Override
    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new AuthenticationException();
        }
    }

    @Override
    public LoginMember toLoginMember() {
        return new LoginMember(id, email, password, age);
    }
}
