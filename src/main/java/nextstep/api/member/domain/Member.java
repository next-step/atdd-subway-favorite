package nextstep.api.member.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer age;
    private String role;

    private Member(final String email, final String password, final Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.role = RoleType.ROLE_MEMBER.name();
    }

    public static Member basic(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }

    public static Member oAuth2User(final String email, final Integer age) {
        return new Member(email, "", age);
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }
}
