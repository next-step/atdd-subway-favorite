package nextstep.member.domain;

import nextstep.member.domain.exception.CantAddFavoriteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static nextstep.member.domain.exception.CantAddFavoriteException.ALREADY_ADDED;
import static nextstep.member.domain.exception.CantAddFavoriteException.INVALID_SOURCE_AND_TARGET;

@Embeddable
public class Favorites {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Favorite> favorites = new ArrayList<>();

    public void add(Favorite favorite) {
        Long source = favorite.getSource();
        Long target = favorite.getTarget();

        if (source.equals(target)) {
            throw new CantAddFavoriteException(INVALID_SOURCE_AND_TARGET);
        }

        favorites.stream()
                .filter(it -> it.match(source, target))
                .findAny()
                .ifPresent(it -> {
                    throw new CantAddFavoriteException(ALREADY_ADDED);
                });

        favorites.add(favorite);
    }

    public void delete(Long id) {
        favorites.removeIf(it -> it.matchId(id));
    }

    public Favorite matchingFavorite(Long source, Long target) {
        return favorites.stream()
                .filter(it -> it.match(source, target))
                .findAny()
                .orElseThrow();
    }

    List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
