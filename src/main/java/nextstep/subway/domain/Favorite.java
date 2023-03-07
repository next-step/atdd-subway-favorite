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

    public Favorite(final long memberId, final long sourceStationId, final long targetStationId) {
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(final FavoriteRepository favoriteRepository, final long memberId, final long sourceStationId, final long targetStationId) {
        validateBeforeCreate(favoriteRepository, memberId, sourceStationId, targetStationId);
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

    private void validateBeforeCreate(final FavoriteRepository favoriteRepository, final long memberId, final long sourceStationId, final long targetStationId) {
        favoriteRepository.findByMemberIdAndSourceStationIdAndTargetStationId(memberId, sourceStationId, targetStationId)
                .ifPresent(result -> {
                    throw new RuntimeException();
                });
    }
}
