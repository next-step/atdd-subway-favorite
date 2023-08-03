package nextstep.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public Favorites() {
    }

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void removeFavorite(Long favoriteId) {
        Favorite favorite = getFavorite(favoriteId);
        this.favorites.remove(favorite);
    }

    public Favorite getFavorite(Long favoriteId) {
        return this.favorites.stream()
                .filter(it -> Objects.equals(it.getId(), favoriteId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 즐겨찾기가 해당 사용자에 존재하지 않습니다."));
    }

    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
