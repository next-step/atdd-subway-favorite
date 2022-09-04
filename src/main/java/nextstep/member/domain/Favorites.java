package nextstep.member.domain;

import nextstep.member.domain.exception.FavoriteException;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static nextstep.member.domain.exception.FavoriteException.DUPLICATION_FAVORITE;

@Embeddable
public class Favorites {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Favorite> favorites = new ArrayList<>();

    public List<Favorite> getFavorites() {
        return this.favorites;
    }

    public void add(Favorite favorite) {
        checkDuplicationFavorite(favorite);
        this.favorites.add(favorite);
    }

    public void delete(Favorite favorite) {
        this.favorites.remove(favorite);
    }

    private void checkDuplicationFavorite(Favorite favorite) {
        favorites.stream()
                .filter(it -> it.hasDuplicateFavorite(favorite.getTarget(), favorite.getSource()))
                .findFirst()
                .ifPresent(it -> {
                    throw new FavoriteException(DUPLICATION_FAVORITE);
                });
    }
}
