package nextstep.subway.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = REMOVE, orphanRemoval = true)
    private final List<Favorite> favoriteList = new ArrayList<>();

    public List<Favorite> getList() {
        return new ArrayList<>(favoriteList);
    }
}
