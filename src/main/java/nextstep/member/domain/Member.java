package nextstep.member.domain;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.exception.FavoriteNotFoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Favorite> favorites = new ArrayList<>();

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

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
        favorite.setUpMember(this);
    }

    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }

    public void removeFavorite(Long id) {
        Favorite findedFavorite = findFavorite(id);
        favorites.remove(findedFavorite);
    }

    private Favorite findFavorite(Long id) {
        return favorites.stream()
                .filter(favorite -> favorite.hasEqualId(id))
                .findAny()
                .orElseThrow(FavoriteNotFoundException::new);
    }
}
