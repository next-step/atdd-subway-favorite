package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.exception.FavoriteAlreadyExistException;
import nextstep.subway.member.domain.Member;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Favorite> favorites = new HashSet<>();

    public Favorites() {
    }

    public void add(Member member, Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new FavoriteAlreadyExistException();
        }
        favorites.add(favorite);
        member.updateFavorites(this);
    }

    public Set<Favorite> getFavorites() {
        return Collections.unmodifiableSet(favorites);
    }
}
