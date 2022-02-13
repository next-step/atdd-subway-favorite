package nextstep.subway.domain.member;

import nextstep.subway.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {
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
        if(verifyChange(member.getEmail(), this.email)) {
            this.email = member.email;
        }
        if(verifyChange(member.getPassword(), this.password)) {
            this.password = member.password;
        }
        if(verifyChange(member.getAge(), this.age)) {
            this.age = member.age;
        }
    }

    private boolean verifyChange(Object target, Object originalValue) {
        return target != null && !target.equals(originalValue);
    }
}
