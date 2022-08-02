package nextstep.member.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member")
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() { }

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public List<Favorite> allFavorites() {
        return unmodifiableList(favorites);
    }

}
