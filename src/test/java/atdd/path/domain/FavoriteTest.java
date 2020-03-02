package atdd.path.domain;

import atdd.path.SoftAssertionTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import static atdd.path.TestConstant.TEST_STATION;
import static atdd.path.dao.FavoriteDao.EDGE_TYPE;


public class FavoriteTest extends SoftAssertionTest {

    @DisplayName("시작역광 종착역이 같을 때 오류를 리턴하는지")
    @Test
    public void cannotSourceStationSameWithTargetStation() {
        Edge edge = new Edge(0L, TEST_STATION, TEST_STATION, 10);

        Assertions.assertThrows(DuplicateKeyException.class,
                () -> Favorite.checkSourceAndTargetStationIsSame(edge, EDGE_TYPE));
    }
}


