package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    @JsonIgnore
    @Embedded
    private Edges edges;

    public Line(Long id, String name) {
        this(id, name, Collections.EMPTY_LIST, null, null, 0);
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this(id, name, Collections.EMPTY_LIST, startTime, endTime, intervalTime);
    }

    public Line(Long id, String name, List<Edge> edges, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.edges = new Edges(edges);
    }

    public static Line of(String name, LocalTime startTime, LocalTime endTime, int interval) {
        return new Line(name, startTime, endTime, interval);
    }

    private Line(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this(null, name, Collections.EMPTY_LIST, startTime, endTime, intervalTime);
    }

    public List<Edge> getEdges() {
        return edges.getEdges();
    }

    public List<Station> getStations() {
        return edges.getStations();
    }

    public void addEdge(Edge edge) {
        this.edges = this.edges.add(edge);
    }

    public Edges removeStation(Station station) {
        this.edges = this.edges.removeStation(station);
        return this.edges;
    }

}
