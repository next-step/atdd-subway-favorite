package nextstep.member.domain;

import nextstep.member.config.exception.PasswordMatchException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static nextstep.member.config.message.MemberError.UNAUTHORIZED;

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

    protected Member() {}

    public Member(final String email, final String password, final Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = List.of(RoleType.ROLE_MEMBER.name());
    }

    public Member(final String email, final String password, final Integer age, final List<String> roles) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }

    public void update(final Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void validatePassword(String password) {
        if (!Objects.equals(this.password, password)) {
            throw new PasswordMatchException(UNAUTHORIZED);
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

    public List<String> getRoles() {
        return roles;
    }
}
