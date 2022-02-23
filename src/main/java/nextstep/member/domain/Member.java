package nextstep.member.domain;


import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Favorites;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    protected Member() {
    }

    @Embedded
    private Favorites favorites = new Favorites();

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public void addFavorite(Favorite favorite) {
        favorites.addFavorite(favorite);
    }

    public Favorites getFavorites() {
        return favorites;
    }

    public boolean isFavoriteOwner(Long favoriteId) {
        return favorites.getFavorites().stream().anyMatch(favorite -> {
            return favorite.getId() == favoriteId;
        });
    }
}
