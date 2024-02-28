package nextstep.core.member.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "age", nullable = true)
    private Integer age;

    public Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public boolean isSameId(Long memberId) {
        return this.id.equals(memberId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(age, member.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, age);
    }
}
