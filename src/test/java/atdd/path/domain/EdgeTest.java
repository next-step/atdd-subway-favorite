package atdd.path.domain;

import atdd.path.SoftAssertionTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;


public class EdgeTest extends SoftAssertionTest {

    @DisplayName("시작역광 종착역이 같은지 확인 가능한지")
    @Test
    public void isSameNameWithSourceAndTarget() {
        Edge edge = new Edge(0L, TEST_STATION, TEST_STATION, 10);

        assertThat(edge.isSameNameWithSourceAndTarget()).isTrue();
    }

    @DisplayName("시작역광 종착역이 같을 때 오류를 리턴하는지")
    @Test
    public void cannotSourceStationSameWithTargetStation() {
        Edge favoriteEdge = new Edge(0L, TEST_STATION, TEST_STATION, 10);
        Assertions.assertThrows(DuplicateKeyException.class,
                favoriteEdge::checkSourceAndTargetStationIsSameWhenEdge);
    }

    @DisplayName("시작역과 종착역이 같을 때 연결되어 있지 않을 때 예외를 처리하는지")
    @Test
    public void cannotFavoriteEdgeWhenNotConnectSourceWithTarget() {
        Edge favoriteEdge = new Edge(EDGE_ID_15, TEST_STATION_2, TEST_STATION_17, 10);
        Assertions.assertThrows(IllegalArgumentException.class,
                favoriteEdge::checkBidirectionalSourceAndTarget);
    }
}


