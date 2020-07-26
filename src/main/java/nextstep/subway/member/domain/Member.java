package nextstep.subway.member.domain;

import nextstep.subway.config.BaseEntity;
import nextstep.subway.favorite.domain.Favorite;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    @OneToMany(mappedBy = "member", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Favorite> favorites = new ArrayList<>();

    public Member() {
    }

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
        this.favorites.add(favorite);
        favorite.setMember(this);
    }

    public List<Favorite> findAllFavorite() {
        return favorites;
    }

    public void deleteFavorite(Favorite favorite) {
        this.favorites.remove(favorite);
        favorite.setMember(null);
    }
}
