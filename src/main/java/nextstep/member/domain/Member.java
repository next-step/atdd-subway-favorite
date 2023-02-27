package nextstep.member.domain;

import nextstep.member.domain.exception.InvalidUserInfoException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "MEMBER_ROLE",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "id")
    )
    @Column(name = "role")
    private List<String> roles;

    public Member() {
    }

    private Member(Long id, String email, String password, Integer age, List<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }

    public Member(String email) {
        this(null, email, null, null, List.of(RoleType.ROLE_MEMBER.name()));
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age, List.of(RoleType.ROLE_MEMBER.name()));
    }

    public Member(String email, String password, Integer age, List<String> roles) {
        this(null, email, password, age, roles);
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

    public List<String> getRoles() {
        return roles;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void checkPassword(String password) {
        if (!Objects.equals(this.password, password)) {
            throw new InvalidUserInfoException();
        }
    }
}
