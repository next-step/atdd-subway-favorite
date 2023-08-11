package nextstep.member.domain;

import nextstep.member.application.exception.ErrorCode;
import nextstep.member.application.exception.FavoriteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Favorite> favorites;

    public Favorites() {
        this.favorites = new ArrayList<>();
    }

    public Favorites(Favorite favorite) {
        this.favorites = new ArrayList<>();
        this.favorites.add(favorite);
    }

    public void add(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void remove(Long id) {
        Favorite target = this.favorites.stream()
                .filter(favorite -> Objects.equals(favorite.getId(), id))
                .findFirst()
                .orElseThrow(() -> new FavoriteException(ErrorCode.CANNOT_DELETE_NOT_EXIST_FAVORITE));
        favorites.remove(target);
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }
}
