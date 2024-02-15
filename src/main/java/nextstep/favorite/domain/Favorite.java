package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    protected Favorite() {
    }

    public Favorite(final Long memberId, final Long sourceId, final Long targetId) {
        this.memberId = memberId;
        this.sourceStationId = sourceId;
        this.targetStationId = targetId;
    }
}
