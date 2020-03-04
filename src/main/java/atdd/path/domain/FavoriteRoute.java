package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class FavoriteRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    @Builder
    public FavoriteRoute(Long id, Long userId, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.userId = userId;
        this.sourceStation = new Station(sourceStationId, null);
        this.targetStation = new Station(targetStationId, null);
    }
}
