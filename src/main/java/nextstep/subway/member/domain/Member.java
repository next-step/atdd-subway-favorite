package nextstep.subway.member.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    @Embedded
    private Favorites favorites = new Favorites();

    public Member() {
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public void addFavorite(Favorite favorite) {
        favorites.add(this, favorite);
    }

    public void updateFavorites(Favorites favorites) {
        this.favorites = favorites;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void validatePassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new RuntimeException();
        }
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

    public List<Favorite> getFavorites() {
        return favorites.getFavorites();
    }
}
