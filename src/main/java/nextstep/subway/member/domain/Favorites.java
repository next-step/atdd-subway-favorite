package nextstep.subway.member.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Favorite> favorites = new ArrayList<>();

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public void delete(Long id) {
        Optional<Favorite> optFavorite = favorites.stream().filter(favorite ->
                favorite.getId() == id
        ).findFirst();

        Favorite favorite = optFavorite.orElseThrow(() -> new RuntimeException());
        favorites.remove(favorite);
    }
}
