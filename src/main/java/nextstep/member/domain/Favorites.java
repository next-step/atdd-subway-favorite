package nextstep.member.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.auth.AuthenticationException;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favoriteCollection;

    public Favorites() {
        this.favoriteCollection = new ArrayList<>();
    }

    public void add(Favorite favorite) {
        favoriteCollection.add(favorite);
    }

    public void delete(Favorite favorite) {
        if (!favoriteCollection.contains(favorite)) {
            throw new AuthenticationException();
        }
        favoriteCollection.remove(favorite);
    }
}
