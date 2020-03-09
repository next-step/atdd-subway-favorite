package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class FavoriteStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", referencedColumnName = "id")
    private Station station;

    @Builder
    public FavoriteStation(Long id, Long userId, Long stationId) {
        this.id = id;
        this.userId = userId;
        this.station = new Station(stationId, null);
    }

    public Long getStationId() {
        return this.station.getId();
    }
}
