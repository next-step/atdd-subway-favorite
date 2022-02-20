package nextstep.member.domain;


import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();


    public void add(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        this.favorites.remove(favorite);
    }

    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
