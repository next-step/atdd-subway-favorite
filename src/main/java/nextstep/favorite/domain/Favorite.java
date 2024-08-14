package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

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

    public Favorite(Long id, Long sourceStationId, Long targetStationId, Long memberId) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) && Objects.equals(sourceStationId, favorite.sourceStationId) && Objects.equals(targetStationId, favorite.targetStationId) && Objects.equals(memberId, favorite.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceStationId, targetStationId, memberId);
    }
}
