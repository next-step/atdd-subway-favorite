package nextstep.subway.member.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.function.Consumer;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    @Embedded
    private Favorites favorites;

    public Member() {
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.favorites = new Favorites();
    }

    public Member(Long id, String email, String password, Integer age, Favorites favorites) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
        this.favorites = favorites;
    }

    public Favorites getFavorites() {
        return favorites;
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

    public void forEachFromFavorite(Consumer<Favorite> consumer) {
        favorites.forEach(consumer);
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
        this.favorites = member.favorites;
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new RuntimeException();
        }
    }

    public void addFavorite(Favorite favorite) {
        favorites.insert(this, favorite);
    }

    public void deleteFavorite(Long favoriteId) {
        favorites.delete(favoriteId);
    }
}
