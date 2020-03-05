package atdd.path.domain;

import atdd.path.SoftAssertionTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import static atdd.path.TestConstant.TEST_STATION;
import static atdd.path.dao.FavoriteDao.EDGE_TYPE;
import static atdd.path.fixture.UserFixture.NEW_USER;


public class FavoriteTest extends SoftAssertionTest {

    @DisplayName("시작역광 종착역이 같을 때 오류를 리턴하는지")
    @Test
    public void cannotSourceStationSameWithTargetStation() {
        Edge edge = new Edge(0L, TEST_STATION, TEST_STATION, 10);

        Favorite favorite = new Favorite(0L, NEW_USER, edge);
        Assertions.assertThrows(DuplicateKeyException.class,
                () -> favorite.checkSourceAndTargetStationIsSame(EDGE_TYPE));
    }
}


