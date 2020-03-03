package atdd.path.domain;

import atdd.path.SoftAssertionTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import static atdd.path.TestConstant.*;
import static atdd.path.dao.FavoriteDao.EDGE_TYPE;


public class ItemTest extends SoftAssertionTest {

    @DisplayName("시작역광 종착역이 같을 때 오류를 리턴하는지")
    @Test
    public void cannotSourceStationSameWithTargetStation() {
        Item item = new Edge(0L, TEST_STATION, TEST_STATION, 10);
        Assertions.assertThrows(DuplicateKeyException.class,
                () -> item.checkSourceAndTargetStationIsSameWhenEdge(EDGE_TYPE));
    }

    @DisplayName("시작역과 종착역이 같을 때 연결되어 있지 않을 때 예외를 처리하는지")
    @Test
    public void cannotFavoriteEdgeWhenNotConnectSourceWithTarget() {
        Item item = new Edge(EDGE_ID_15, TEST_STATION_2, TEST_STATION_17, 10);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> item.checkConnectSourceAndTarget(EDGE_TYPE));
    }
}


