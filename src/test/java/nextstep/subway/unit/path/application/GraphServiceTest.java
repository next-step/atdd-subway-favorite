package nextstep.subway.unit.path.application;

import static nextstep.Fixtures.강남_역삼_구간;
import static nextstep.Fixtures.이호선;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.path.application.GraphService;
import nextstep.subway.path.domain.LineSectionEdge;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("경로 그래프 서비스 단위 테스트")
@SuppressWarnings("NonAsciiCharacters")
class GraphServiceTest {
  @Mock LineService lineService;
  @InjectMocks GraphService graphService;

  @DisplayName("노선 정보를 바탕으로 그래프를 불러온다.")
  @Test
  void loadGraph() {
    Line 이호선 = 이호선();
    LineSection 강남_역삼_구간 = 강남_역삼_구간();
    Station 강남역 = 강남_역삼_구간.getUpStation();
    Station 역삼역 = 강남_역삼_구간.getDownStation();
    given(lineService.findAllLines()).willReturn(List.of(이호선));

    SubwayGraph graph = graphService.loadGraph();

    SubwayGraph expectedGraph =
        new SubwayGraph(
            WeightedMultigraph.<Station, LineSectionEdge>builder(LineSectionEdge.class)
                .addVertex(강남역)
                .addVertex(역삼역)
                .addEdge(강남역, 역삼역, LineSectionEdge.of(강남_역삼_구간), 강남_역삼_구간.getDistance())
                .build());
    assertThat(graph.isSame(expectedGraph)).isTrue();
  }
}
