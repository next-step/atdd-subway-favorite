package nextstep.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.application.exception.InvalidPasswordException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Favorites;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;
import static nextstep.member.domain.RoleType.ROLE_MEMBER;

@Entity
@NoArgsConstructor(access = PROTECTED)
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

    @Embedded
    @Getter(NONE)
    private final Favorites favorites = new Favorites();

    public Member(final String email, final String password, final Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = List.of(ROLE_MEMBER.name());
    }

    public Member(final String email, final String password, final Integer age, final List<String> roles) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }

    public static Member createMemberThroughOauth(final String email) {
        return new Member(email, "", null, List.of(ROLE_MEMBER.name()));
    }

    public void update(final Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void validatePassword(final String password) {
        if (!Objects.equals(this.password, password)) {
            throw new InvalidPasswordException();
        }
    }

    public List<Favorite> getFavoriteList() {
        return this.favorites.getList();
    }
}
