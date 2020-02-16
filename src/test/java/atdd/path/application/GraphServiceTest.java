package atdd.path.application;

import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.repository.LineRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = GraphService.class)
public class GraphServiceTest {
    private GraphService graphService;

    @MockBean
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        this.graphService = new GraphService(lineRepository);
    }

    @DisplayName("출발역과 도착역 사이의 최단경로 Station 목록을 응답받기")
    @Test
    public void findPath() {
        Long startId = 1L;
        Long endId = 3L;
        List<Line> lines = Lists.list(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);
        given(lineRepository.findAll()).willReturn(lines);

        List<Station> shortestPath = graphService.findPath(startId, endId);

        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(shortestPath.get(0)).isEqualTo(TEST_STATION);
        assertThat(shortestPath.get(2)).isEqualTo(TEST_STATION_3);
    }
}
