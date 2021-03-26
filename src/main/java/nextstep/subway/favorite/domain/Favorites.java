package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.exception.FavoriteAlreadyExistException;
import nextstep.subway.member.domain.Member;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public Favorites() {
    }

    public void add(Member member, Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new FavoriteAlreadyExistException();
        }
        favorites.add(favorite);
        member.updateFavorites(this);
    }

    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
