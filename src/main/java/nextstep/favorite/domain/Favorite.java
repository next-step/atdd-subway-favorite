package nextstep.favorite.domain;

import nextstep.member.domain.Member;

import javax.persistence.*;
import java.util.Objects;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_favorite_member_id_source_station_id_target_station_id",
                        columnNames = {"member_id", "source_station_id", "target_station_id"}
                )
        }
)
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "source_station_id")
    private Long sourceStationId;

    @Column(name = "target_station_id")
    private Long targetStationId;

    protected Favorite() {
    }

    public Favorite(Long memberId, Long sourceStationId, Long targetStationId) {
        this(null, memberId, sourceStationId, targetStationId);
    }

    public Favorite(Long id, Long memberId, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getId() {
        return id;
    }

    public void validateOwner(Member member) {
        if (!memberId.equals(member.getId())) {
            throw new IllegalStateException("즐겨찾기를 삭제할 권한이 없습니다.");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
