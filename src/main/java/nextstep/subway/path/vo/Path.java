package nextstep.subway.path.vo;

import lombok.Getter;
import nextstep.subway.station.entity.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

@Getter
public class Path {

    private List<Station> stations;

    private Integer distance;

    public Path(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        this.stations = graphPath.getVertexList();
        this.distance = (int) graphPath.getWeight();
    }

}
