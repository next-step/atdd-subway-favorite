package nextstep.subway.unit.path.domain;

import static nextstep.Fixtures.*;
import static org.assertj.core.api.Assertions.*;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.path.domain.LineSectionEdge;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 그래프 단위 테스트")
class SubwayGraphTest {
  private final Station 교대역 = 교대역();
  private final Station 강남역 = 강남역();
  private final Station 남부터미널역 = 남부터미널역();
  private final Station 양재역 = 양재역();

  @DisplayName("역을 추가한다.")
  @Test
  void addStation() {
    SubwayGraph graph = new SubwayGraph();

    graph.addStation(교대역);

    assertThat(graph.isSame(new SubwayGraph())).isFalse();
  }

  @DisplayName("중복해서 역을 추가해도 그래프는 변하지 않는다.")
  @Test
  void duplicateAddStation() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(교대역);

    graph.addStation(교대역);

    assertThat(
            graph.isSame(
                new SubwayGraph(
                    WeightedMultigraph.<Station, LineSectionEdge>builder(LineSectionEdge.class)
                        .addVertex(교대역)
                        .build())))
        .isTrue();
  }

  @DisplayName("구간을 추가한다.")
  @Test
  void addLineSection() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(교대역);
    graph.addStation(강남역);

    graph.addLineSection(LineSection.of(교대역, 강남역, 10));

    assertThat(graph.isSame(new SubwayGraph())).isFalse();
  }

  @DisplayName("Multigraph 이기에 중복해서 구간을 추가할 수 있다.")
  @Test
  void duplicateAddLineSection() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(교대역);
    graph.addStation(강남역);
    LineSection 교대_강남_구간 = LineSection.of(교대역, 강남역, 10);
    graph.addLineSection(교대_강남_구간);

    graph.addLineSection(교대_강남_구간);

    assertThat(
            graph.isSame(
                new SubwayGraph(
                    WeightedMultigraph.<Station, LineSectionEdge>builder(LineSectionEdge.class)
                        .addVertices(교대역, 강남역)
                        .addEdge(교대역, 강남역, LineSectionEdge.of(교대_강남_구간), 10)
                        .addEdge(교대역, 강남역, LineSectionEdge.of(교대_강남_구간), 10)
                        .build())))
        .isTrue();
  }

  @DisplayName("추가하는 구간의 상/하행역이 존재하지 않을 때 예외를 던진다.")
  @Test
  void addLineSectionShouldThrowExceptionWhenStationNotExist() {
    SubwayGraph graph = new SubwayGraph();
    LineSection section = LineSection.of(교대역, 강남역, 10);
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> graph.addLineSection(section));
  }

  @DisplayName("노선을 추가한다.")
  @Test
  void addLine() {
    SubwayGraph graph = new SubwayGraph();
    Line 이호선 = 이호선();
    Station 역삼역 = 역삼역();

    graph.addLine(이호선);

    assertThat(
            graph.isSame(
                new SubwayGraph(
                    WeightedMultigraph.<Station, LineSectionEdge>builder(LineSectionEdge.class)
                        .addVertices(강남역, 역삼역)
                        .addEdge(강남역, 역삼역, LineSectionEdge.of(강남_역삼_구간()), 10)
                        .build())))
        .isTrue();
  }

  @Nested
  @DisplayName("최단 경로 조회 단위 테스트")
  class ShortestPathTest {
    private SubwayGraph graph;

    @BeforeEach
    void setUp() {
      graph = new SubwayGraph();
      graph.addStation(교대역);
      graph.addStation(강남역);
      graph.addStation(양재역);
      graph.addStation(남부터미널역);
      graph.addLineSection(LineSection.of(교대역, 강남역, 10));
      graph.addLineSection(LineSection.of(강남역, 양재역, 10));
      graph.addLineSection(LineSection.of(교대역, 남부터미널역, 2));
      graph.addLineSection(LineSection.of(남부터미널역, 양재역, 3));
    }

    @DisplayName("최단 거리 경로를 조회한다.")
    @Test
    void getShortestPath() {
      Path path = graph.getShortestPath(교대역, 양재역);

      assertThat(path.getTotalDistance()).isEqualTo(5);
      assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @DisplayName("출발역과 도착역이 같은 경우 역 하나만이 반환된다.")
    @Test
    void sourceAndSinkAreTheSame() {
      Path path = graph.getShortestPath(교대역, 교대역);
      assertThat(path.getTotalDistance()).isZero();
      assertThat(path.getStations()).containsExactly(교대역);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void sourceAndSinkDisconnected() {
      Station 판교역 = 판교역();
      graph.addStation(판교역);

      Path path = graph.getShortestPath(교대역, 판교역);

      assertThat(path.getTotalDistance()).isZero();
      assertThat(path.getStations()).isEmpty();
    }

    @DisplayName("출발역이나 도착역이 존재하지 않는 경우")
    @Test
    void sourceOrSinkNotExist() {
      Station 판교역 = 판교역();
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> graph.getShortestPath(교대역, 판교역));
    }
  }
}
