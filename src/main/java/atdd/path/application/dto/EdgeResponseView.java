package atdd.path.application.dto;

import atdd.path.domain.Edge;
import atdd.path.domain.Edges;

import java.util.List;
import java.util.stream.Collectors;

public class EdgeResponseView {
    private Long id;
    private StationResponseView sourceStationView;
    private StationResponseView targetStationView;
    private int distance;

    public EdgeResponseView() {
    }

    public static EdgeResponseView of(Edge edge) {
        return new EdgeResponseView(edge);
    }

    public static List<EdgeResponseView> listOf(Edges edges) {
        return edges.getEdges().stream()
                .map(it -> new EdgeResponseView(it))
                .collect(Collectors.toList());
    }

    private EdgeResponseView(Edge edge) {
        this.id = edge.getId();
        this.sourceStationView = StationResponseView.of(edge.getSourceStation());
        this.targetStationView = StationResponseView.of(edge.getTargetStation());
        this.distance = edge.getDistance();
    }

    public Long getId() {
        return id;
    }

    public StationResponseView getSourceStationView() {
        return sourceStationView;
    }

    public StationResponseView getTargetStationView() {
        return targetStationView;
    }

    public int getDistance() {
        return distance;
    }
}
