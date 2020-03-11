package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@ToString(exclude = {"line", "sourceStation", "targetStation"})
@NoArgsConstructor
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    private int distance;

    public Edge(Long id, Station sourceStation, Station targetStation, int distance) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public static Edge of(Station sourceStation, Station targetStation, int distance) {
        return new Edge(null, sourceStation, targetStation, distance);
    }

    public boolean hasStation(Station station) {
        return sourceStation.equals(station) || targetStation.equals(station);
    }
}
