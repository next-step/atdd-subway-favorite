package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id")
    @JsonIgnore
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id")
    @JsonIgnore
    private Station targetStation;

    private int distance;

    public Edge() {
    }

    public Edge(Long id, Station sourceStation, Station targetStation, int distance) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public static Edge of(Station sourceStation, Station targetStation, int distance) {
        return new Edge(null, sourceStation, targetStation, distance);
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public boolean hasStation(Station station) {
        return sourceStation.equals(station) || targetStation.equals(station);
    }
}
