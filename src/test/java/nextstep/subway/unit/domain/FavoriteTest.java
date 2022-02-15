package nextstep.subway.unit.domain;

import nextstep.exception.NotConnectedException;
import nextstep.exception.SameStationException;
import nextstep.subway.domain.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.unit.FavoriteUnitTestHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    @BeforeEach
    void setFixtures() {
        createLines();
        createStations();
        createSections();
        createMember();
    }

    @Test
    void 두_역이_같으면_즐겨찾기_등록에_실패() {
        Favorite favorite = Favorite.of(강남역, 강남역, member);
        assertThatThrownBy(() -> favorite.validateSave()).isInstanceOf(SameStationException.class);
    }

    @Test
    void 두_역이_연결되어있지_않으면_등록에_실패() {
        Favorite favorite = Favorite.of(강남역, 판교역, member);
        assertThatThrownBy(() -> favorite.validateSave()).isInstanceOf(NotConnectedException.class);
    }
}
