package atdd.path.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Entity
public class FavoriteRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long sourceStationId;

    private Long targetStationId;

    @Builder
    public FavoriteRoute(Long id, Long userId, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.userId = userId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }


}
