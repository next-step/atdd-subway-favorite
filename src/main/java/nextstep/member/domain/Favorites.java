package nextstep.member.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Favorites {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Favorite> favorites = new ArrayList<>();

    public List<Favorite> getFavorites() {
        return this.favorites;
    }

    public void add(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void delete(Favorite favorite) {
        this.favorites.remove(favorite);
    }
}
