package nextstep.member.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long source;
    private Long target;

    protected Favorite() {
    }

    public Favorite(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public boolean matchId(Long id) {
        return this.id.equals(id);
    }

    public boolean match(Long source, Long target) {
        return this.source.equals(source) && this.target.equals(target);
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
}
