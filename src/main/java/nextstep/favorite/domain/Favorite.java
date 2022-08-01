package nextstep.favorite.domain;

import javax.persistence.*;

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

    public Favorite(final Long memberId, final Long source, final Long target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public boolean isOwner(final Long memberId) {
        return this.memberId.equals(memberId);
    }
}
