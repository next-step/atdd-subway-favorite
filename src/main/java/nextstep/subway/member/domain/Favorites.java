package nextstep.subway.member.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "memberId", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Favorite> favorites = new ArrayList<>();

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public void delete(Long id) {
        Favorite favorite = favorites.stream()
                .filter(fav -> fav.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("삭제하려는 즐겨찾기가 없습니다"));

        favorites.remove(favorite);
    }
}
