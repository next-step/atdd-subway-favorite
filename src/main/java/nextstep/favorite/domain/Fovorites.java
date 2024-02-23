package nextstep.favorite.domain;

import nextstep.exception.NotFoundFavoriteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Fovorites {

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public Fovorites() {
    }

    private Fovorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public static Fovorites from(List<Favorite> favorites) {
        return new Fovorites(favorites);
    }

    public void add(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void deleteFavorite(Favorite favorite) {
        if (!favorites.contains(favorite)) {
            throw new NotFoundFavoriteException();
        }

        favorite.delete();
        this.favorites.remove(favorite);
    }

    public List<Favorite> getFavorites() {
        return this.favorites;
    }

}
