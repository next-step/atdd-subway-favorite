package subway.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor
public class MemberFavorites {

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Favorite> favorites = new ArrayList<>();

    public void add(Favorite newFavorite, Member member) {
        validAlreadyExist(newFavorite);

        newFavorite.setMember(member);
        add(newFavorite);
    }

    public boolean isExistFavorite(Favorite favorite) {
        return favorites.contains(favorite);
    }

    public void remove(Favorite favorite) {
        this.favorites.remove(favorite);
    }

    private void validAlreadyExist(Favorite newFavorite) {
        if (findFavoriteBySourceAndTarget(newFavorite)) {
            throw new SubwayBadRequestException(SubwayMessage.FAVORITE_IS_ALREADY_EXIST);
        }
    }

    private boolean findFavoriteBySourceAndTarget(Favorite newFavorite) {
        return favorites.stream()
                .anyMatch(favorite -> favorite.getSourceStation().equals(newFavorite.getSourceStation()) || favorite.getTargetStation().equals(newFavorite.getTargetStation()));
    }

    private void add(Favorite newFavorite) {
        this.favorites.add(newFavorite);
    }
}
