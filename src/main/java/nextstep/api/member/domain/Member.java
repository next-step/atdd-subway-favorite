package nextstep.api.member.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import nextstep.api.auth.domain.UserDetails;

@Entity
@Getter
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(String email) {
        this.email = email;
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

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }


    public boolean isNot(Long id){
        return !is(id);
    }

    public boolean is(Long id) {
        return this.id !=null && this.id.equals(id) ;
    }
}
