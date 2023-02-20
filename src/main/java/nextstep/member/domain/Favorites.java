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
    private List<Favorite> values = new ArrayList<>();

    public void add(Favorite favorite) {
        this.values.add(favorite);
    }


    public void delete(Favorite favorite) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (!this.values.contains(favorite)) {
            throw new IllegalArgumentException();
        }
        this.values.remove(favorite);
    }

    public List<Favorite> values() {
        return Collections.unmodifiableList(values);
    }
}
