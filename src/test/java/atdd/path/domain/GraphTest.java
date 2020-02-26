package atdd.path.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GraphTest {
    public static final List<Line> LINES = Lists.list(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);

    @Test
    public void createSubwayGraph() {
        Graph graph = new Graph(LINES);

        assertThat(graph.getLines().size()).isEqualTo(4);
    }

    @Test
    public void getPath() {
        Graph graph = new Graph(LINES);

        List<Station> result = graph.getShortestDistancePath(STATION_ID, STATION_ID_3);

        assertThat(result.get(0)).isEqualTo(TEST_STATION);
        assertThat(result.get(2)).isEqualTo(TEST_STATION_3);
    }

    @DisplayName("시작점과 끝점이 연결된 선이 있는지 확인해야 한다")
    @Test
    void mustPathExists() {
        Graph graph = new Graph(List.of(TEST_LINE));

        boolean isConnect = graph.isPathExists(STATION_ID, STATION_ID_22);
        assertThat(isConnect).isFalse();
    }

}
