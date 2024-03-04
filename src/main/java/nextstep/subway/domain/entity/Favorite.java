package nextstep.subway.domain.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long source;

    private Long target;

    protected Favorite() {
    }

    public Favorite(Long memberId, Long source, Long target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Favorite) {
            Favorite favorite = (Favorite) obj;
            return Objects.equals(id, favorite.getId());
        }

        return false;
    }
}
