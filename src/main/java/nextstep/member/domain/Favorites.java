package nextstep.member.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import nextstep.member.application.exception.UnAuthorizedException;

@Embeddable
public class Favorites {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "favorite_id")
    private List<Favorite> favorites = new ArrayList<>();

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void add(final Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void delete(final long id) {
        boolean result = favorites.removeIf(favorite -> favorite.getId() == id);
        if (!result) {
            throw new UnAuthorizedException();
        }
    }
}
