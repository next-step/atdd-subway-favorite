package nextstep.subway.unit.domain;

import nextstep.exception.NotConnectedException;
import nextstep.exception.SameStationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.unit.FavoriteUnitTestHelper.createLines;
import static nextstep.subway.unit.FavoriteUnitTestHelper.createSections;
import static nextstep.subway.unit.FavoriteUnitTestHelper.createStations;
import static nextstep.subway.unit.FavoriteUnitTestHelper.강남역;
import static nextstep.subway.unit.FavoriteUnitTestHelper.판교역;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    @BeforeEach
    void setFixtures() {
        createLines();
        createStations();
        createSections();
    }

    @Test
    void 두_역이_같으면_즐겨찾기_등록에_실패() {
        Favorite favorite = new Favorite(강남역, 강남역);
        assertThatThrownBy(() -> favorite.validateSave()).isInstanceOf(SameStationException.class);
    }

    @Test
    void 두_역이_연결되어있지_않으면_등록에_실패() {
        Favorite favorite = new Favorite(강남역, 판교역);
        assertThatThrownBy(() -> favorite.validateSave()).isInstanceOf(NotConnectedException.class);
    }
}
