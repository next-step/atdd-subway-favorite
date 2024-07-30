package nextstep.study;

import nextstep.subway.line.SectionFixtures;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationFixtures;
import org.assertj.core.api.Assertions;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphtSectionTest {

    private DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    @BeforeEach
    void setUp() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(StationFixtures.강남역);
        graph.addVertex(StationFixtures.양재역);
        graph.addVertex(StationFixtures.논현역);
        graph.addVertex(StationFixtures.고속터미널역);
        graph.addVertex(StationFixtures.교대역);

        graph.setEdgeWeight(graph.addEdge(SectionFixtures.논현_강남.getUpStation(), SectionFixtures.논현_강남.getDownStation()), SectionFixtures.논현_강남.getDistance());
        graph.setEdgeWeight(graph.addEdge(SectionFixtures.논현_고속.getUpStation(), SectionFixtures.논현_고속.getDownStation()), SectionFixtures.논현_고속.getDistance());
        graph.setEdgeWeight(graph.addEdge(SectionFixtures.강남_양재.getUpStation(), SectionFixtures.강남_양재.getDownStation()), SectionFixtures.강남_양재.getDistance());
        graph.setEdgeWeight(graph.addEdge(SectionFixtures.고속_교대.getUpStation(), SectionFixtures.고속_교대.getDownStation()), SectionFixtures.고속_교대.getDistance());
        graph.setEdgeWeight(graph.addEdge(SectionFixtures.교대_양재.getUpStation(), SectionFixtures.교대_양재.getDownStation()), SectionFixtures.교대_양재.getDistance());

        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    @DisplayName("Section 과 Station 을 이용해 최단 거리 찾기")
    @Test
    void SectionAndStation() {
        // given
        // when
        List<Station> shortestPath = dijkstraShortestPath.getPath(StationFixtures.논현역, StationFixtures.양재역).getVertexList();

        // then
        assertThat(shortestPath.size()).isEqualTo(4);
    }

    @DisplayName("source 가 존재하지 않는 역인 경우 테스트")
    @Test
    void notSource() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> dijkstraShortestPath.getPath(StationFixtures.FIRST_UP_STATION, StationFixtures.양재역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("graph must contain the source vertex");
    }

    @DisplayName("sink 가 존재하지 않는 역인 경우 테스트")
    @Test
    void notSink() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> dijkstraShortestPath.getPath(StationFixtures.논현역, StationFixtures.FIRST_UP_STATION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("graph must contain the sink vertex");
    }
}
