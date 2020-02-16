package atdd.path.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Edges {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "edge_id", referencedColumnName = "id")
    @JsonIgnore
    private List<Edge> edgeList = new ArrayList<>();

    public Edges() {
    }

    public Edges(List<Edge> edges) {
        checkValidEdges(edges);
        this.edgeList = edges;
    }

    public List<Edge> getEdges() {
        return edgeList;
    }

    private void checkValidEdges(List<Edge> edges) {
        if (edges.size() == 0) {
            return;
        }

        System.out.println("---stations size---");
        System.out.println(getStations(edges).size());
        System.out.println("---edges size---");
        System.out.println(edges.size());

        if (getStations(edges).size() != edges.size() + 1) {
            throw new RuntimeException();
        }
    }

    public List<Station> getStations() {
        return getStations(this.edgeList);
    }

    private List<Station> getStations(List<Edge> edges) {
        if (edges.size() == 0) {
            return new ArrayList<>();
        }

        List<Station> stations = new ArrayList();
        Station lastStation = findFirstStation(edges);
        stations.add(lastStation);

        while (true) {
            Station nextStation = findNextStationOf(edges, lastStation);
            if (nextStation == null) {
                break;
            }
            stations.add(nextStation);
            lastStation = nextStation;
        }

        return stations;
    }

    private Station findFirstStation(List<Edge> edges) {
        List<Station> targetStations = edges.stream()
                                            .map(Edge::getTargetStation)
                                            .collect(Collectors.toList());

        return edges.stream()
                    .map(Edge::getSourceStation)
                    .filter(it -> !targetStations.contains(it))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
    }

    private Station findNextStationOf(List<Edge> edges, Station firstStation) {
        return edges.stream()
                    .filter(it -> firstStation.equals(it.getSourceStation()))
                    .map(Edge::getTargetStation)
                    .findFirst()
                    .orElse(null);
    }

    public Edges removeStation(Station station) {
        List<Edge> replaceEdge = this.edgeList.stream()
                                              .filter(it -> it.hasStation(station))
                                              .collect(Collectors.toList());

        if (replaceEdge.size() == 0) {
            throw new RuntimeException();
        }

        List<Edge> newEdges = this.edgeList.stream()
                                           .filter(it -> !replaceEdge.contains(it))
                                           .collect(Collectors.toList());

        if (replaceEdge.size() == 1) {
            this.edgeList = newEdges;
            return new Edges(newEdges);
        }

        Edge newEdge = Edge.of(getSourceStationOf(station), getTargetStationOf(station), sum(replaceEdge));
        newEdges.add(newEdge);

        return new Edges(newEdges);
    }

    private Integer sum(List<Edge> replaceEdge) {
        return replaceEdge.stream()
                          .map(it -> it.getDistance())
                          .reduce(0, Integer::sum);
    }

    private Station getSourceStationOf(Station station) {
        return this.edgeList.stream()
                            .filter(it -> station.equals(it.getTargetStation()))
                            .map(it -> it.getSourceStation())
                            .findFirst()
                            .orElseThrow(RuntimeException::new);
    }

    private Station getTargetStationOf(Station station) {
        return this.edgeList.stream()
                            .filter(it -> station.equals(it.getSourceStation()))
                            .map(it -> it.getTargetStation())
                            .findFirst()
                            .orElseThrow(RuntimeException::new);
    }

    public Edges add(Edge edge) {
        List<Edge> newEdges = this.edgeList.stream()
                                           .collect(Collectors.toList());
        newEdges.add(edge);
        return new Edges(newEdges);
    }
}
