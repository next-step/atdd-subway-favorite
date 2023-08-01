package nextstep.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.auth.oauth2.OAuth2UserRequest;
import nextstep.member.constants.RoleType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer age;
    private String role;

    @Builder
    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.role = RoleType.ROLE_MEMBER.name();
    }

    public Member(String email, String password, Integer age, String role) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.role = role;
    }

    public static Member of(OAuth2UserRequest oAuth2UserRequest) {
        return Member.builder()
                .email(oAuth2UserRequest.getUsername())
                .password("")
                .age(oAuth2UserRequest.getAge())
                .build();
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
