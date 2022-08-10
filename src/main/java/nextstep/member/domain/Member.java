package nextstep.member.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
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

    public static Member createAdmin(String email, String password, Integer age) {
        return new Member(email, password, age, List.of(RoleType.ROLE_ADMIN.name(), RoleType.ROLE_MEMBER.name()));
    }

    public static Member createUser(String email, String password, Integer age) {
        return new Member(email, password, age, List.of(RoleType.ROLE_MEMBER.name()));
    }

    public static Member createSubscription(String email, String password, Integer age) {
        return new Member(email, password, age, List.of(RoleType.ROLE_MEMBER.name(), RoleType.ROLE_SUBSCRIPTION_MEMBER.name()));
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = List.of(RoleType.ROLE_MEMBER.name());
    }

    public Member(String email, String password, Integer age, List<String> roles) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }
}
