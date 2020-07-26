package nextstep.subway.member.domain;

import nextstep.subway.config.BaseEntity;
import nextstep.subway.favorite.domain.Favorite;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private final List<Favorite> favorites = new ArrayList<>();

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

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void removeFavorite(Favorite favorite) {
        if (favorite.isOwnedBy(this)) {
            favorites.remove(favorite);
        }
    }
}
