package nextstep.member.domain;

import nextstep.member.domain.exception.NotAuthorizedException;
import nextstep.subway.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    protected Member() {
    }

    public Member(String email) {
        this(email, null, null);
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

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public Favorite addFavorite(Station source, Station target) {
        Favorite favorite = new Favorite(this, source, target);
        favorites.add(favorite);
        return favorite;
    }

    public void deleteFavorite(Favorite favorite) {
        checkMyFavorite(favorite);
        favorites.remove(favorite);
    }

    private void checkMyFavorite(Favorite favorite) {
        if (!favorite.isOwnedBy(this)) {
            throw new NotAuthorizedException(String.format("%s 님의 즐겨찾기가 아닙니다.", email));
        }
    }
}
