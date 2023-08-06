package nextstep.member.domain;

import nextstep.member.application.exception.ErrorCode;
import nextstep.member.application.exception.FavoriteException;
import nextstep.subway.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer age;
    private String role;
    @Embedded
    private Favorites favorites = new Favorites();

    public Member() {
    }

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

    public Member(String email, String password, Integer age, Favorite favorite) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.favorites = new Favorites(favorite);
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

    public String getRole() {
        return role;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public List<Favorite> getFavorites() {
        return favorites.getFavorites();
    }

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void deleteFavorite(Long id) {
        this.favorites.remove(id);
    }
}
