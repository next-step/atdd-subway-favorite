package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;

    @Column(nullable = true)
    private int stationInterval;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edges_id", referencedColumnName = "id", insertable=false, updatable=false)
    @JsonIgnore
    private Edges edges;

    protected Line() {}

    public Line(Long id, String name) {
        this(id, name, Collections.EMPTY_LIST, null, null, 0);
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int stationInterval) {
        this(id, name, Collections.EMPTY_LIST, startTime, endTime, stationInterval);
    }

    public Line(Long id, String name, List<Edge> edges, LocalTime startTime, LocalTime endTime, int stationInterval) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stationInterval = stationInterval;
        this.edges = new Edges(edges);
    }

    public static Line of(String name, LocalTime startTime, LocalTime endTime, int interval) {
        return new Line(name, startTime, endTime, interval);
    }

    private Line(String name, LocalTime startTime, LocalTime endTime, int stationInterval) {
        this(null, name, Collections.EMPTY_LIST, startTime, endTime, stationInterval);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Edge> getEdges() {
        return edges.getEdges();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getStationInterval() {
        return stationInterval;
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
