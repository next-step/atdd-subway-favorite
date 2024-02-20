package nextstep.subway.member.domain;

import nextstep.subway.auth.AuthenticationException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(String email,
                  String password,
                  Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(String email) {
        this(email, "", 0);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public void update(Member findMember,
                       Member member) {
        validAccess(findMember);
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public void validAccess(Member member) {
        if (!this.equals(member)) {
            throw new AuthenticationException("본인의 정보만 수정 할 수 있습니다.");
        }
    }
}
