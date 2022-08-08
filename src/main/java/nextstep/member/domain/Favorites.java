package nextstep.member.domain;

import nextstep.member.domain.exception.CantAddFavoriteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Favorites {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Favorite> favorites = new ArrayList<>();

    public void add(Favorite favorite) {
        Long source = favorite.getSource();
        Long target = favorite.getTarget();

        if (source.equals(target)) {
            throw new CantAddFavoriteException("즐겨찾기의 출발점과 종점이 같을 수 없습니다.");
        }

        favorites.stream()
                .filter(it -> it.match(source, target))
                .findAny()
                .ifPresent(it -> {
                    throw new CantAddFavoriteException("이미 존재하는 즐겨찾기입니다.");
                });

        favorites.add(new Favorite(source, target));
    }

    public void delete(Long id) {
        favorites.removeIf(it -> it.matchId(id));
    }

    public Favorite matchingFavorite(Long source, Long target) {
        return favorites.stream()
                .filter(it -> it.match(source, target))
                .findAny()
                .orElseThrow();
    }

    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
