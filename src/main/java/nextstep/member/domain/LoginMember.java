package nextstep.member.domain;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.domain.UserDetails;

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

    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new AuthenticationException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }
}
