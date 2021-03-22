package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.exception.NotFoundFavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();

    public void remove(long favoriteId) {
        Favorite favorite = favorites.stream()
                                .filter(it -> Objects.equals(it.getId(), favoriteId))
                                .findFirst()
                                .orElseThrow(NotFoundFavoriteException::new);

        favorites.remove(favorite);
    }

    public void add(Member member, Station source, Station target) {
        Favorite favorite = new Favorite(member, source, target);
        favorites.add(favorite);
    }

    public Stream<Favorite> stream() {
        return favorites.stream();
    }
}
