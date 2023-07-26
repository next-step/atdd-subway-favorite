package subway.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer age;
    private RoleType role;

    @Builder.Default
    @Embedded
    private MemberFavorites memberFavorites = new MemberFavorites();

    public void appendFavorite(Favorite newFavorite) {
        this.memberFavorites.add(newFavorite, this);
    }

    public void deleteFavoriteByFavorite(Favorite favorite) {
        if (!this.isExistFavorite(favorite)) {
            throw new SubwayBadRequestException(SubwayMessage.FAVORITE_NOT_MY_OWN);
        }
        memberFavorites.remove(favorite);
    }

    public boolean isExistFavorite(Favorite favorite) {
        return this.memberFavorites.isExistFavorite(favorite);
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }
}
