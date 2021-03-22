package nextstep.subway.favorite.domain;

import nextstep.subway.exceptions.NotFoundFavoriteException;
import nextstep.subway.member.domain.Member;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Favorite> favoriteList;

    public Favorites() {
        this(new ArrayList<>());
    }

    public Favorites(List<Favorite> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public static Favorites of(List<Favorite> favoriteList) {
        return new Favorites(favoriteList);
    }


    public void insert(Member member, Favorite favorite) {
        if (!favoriteList.contains(favorite)) {
            favoriteList.add(favorite);
            favorite.updateMember(member);
        }
    }

    public void delete(Long favoriteId) {
        Favorite foundFavorite = favoriteList.stream().filter(favorite -> favorite.getId().equals(favoriteId))
                .findFirst()
                .orElseThrow(NotFoundFavoriteException::new);

        favoriteList.remove(foundFavorite);
    }

    public void forEach(Consumer<Favorite> consumer) {
        favoriteList.forEach(consumer);
    }
}
