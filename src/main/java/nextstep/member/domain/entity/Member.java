package nextstep.member.domain.entity;

import lombok.Getter;
import nextstep.base.exception.AuthenticationException;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private Integer age;

    protected Member() {
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void verifyPassword(String password) {
        if (!checkPassword(password)) {
            throw new AuthenticationException();
        }
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }
}
