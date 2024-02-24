package nextstep.favorite.domain;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sourceStationId;

    private Long targetStationId;

    private Long memberId;

    public Favorite() {
    }

    public Favorite(Long sourceStationId, Long targetStationId, Long memberId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getMemberId() {
        return memberId;
    }
}