package nextstep.favorites.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public List<Favorite> get() {
        return favorites;
    }
}
