package nextstep.subway.domain;

import javax.persistence.*;

@Entity
@Table(name = "FAVORITE")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "SOURCE_STATION_ID")
    private Long sourceStationId;
    @Column(name = "TARGET_STATION_ID")
    private Long targetStationId;

    protected Favorite() {
    }

    public Favorite(final Long memberId, final Long sourceStationId, final Long targetStationId) {
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
