package atdd.path.domain;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class Line {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int interval;
    private Edges edges;

    public Line(Long id, String name) {
        this(id, name, Collections.EMPTY_LIST, null, null, 0);
    }

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int interval) {
        this(id, name, Collections.EMPTY_LIST, startTime, endTime, interval);
    }

    public Line(Long id, String name, List<Edge> edges, LocalTime startTime, LocalTime endTime, int interval) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
        this.edges = new Edges(edges);
    }

    public static Line of(String name, LocalTime startTime, LocalTime endTime, int interval) {
        return new Line(name, startTime, endTime, interval);
    }

    private Line(String name, LocalTime startTime, LocalTime endTime, int interval) {
        this(null, name, Collections.EMPTY_LIST, startTime, endTime, interval);
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

    public int getInterval() {
        return interval;
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
