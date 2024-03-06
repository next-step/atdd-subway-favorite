package nextstep.member.domain;

import lombok.Builder;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.Favorites;

import javax.persistence.*;
import java.util.Objects;
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer age;
    @Embedded
    private Favorites favorites = new Favorites();

    public Member() {
    }

    @Builder
    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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
        this.favorites.addFavorite(favorite);
    }
}
