package nextstep.favorite.domain;

import nextstep.common.fixture.StationFactory;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FavoriteTest {

    @Test
    @DisplayName("Favorite 객체를 생성할 수 있다.")
    void favoriteCreateTest() {
        final Long memberId = 1L;
        final Station 강남역 = StationFactory.createStation(1L, "강남역");
        final Station 선릉역 = StationFactory.createStation(2L, "선릉역");
        final Favorite favorite = new Favorite(memberId, 강남역, 선릉역);

        assertSoftly(softly -> {
            softly.assertThat(favorite.getMemberId()).isEqualTo(memberId);
            softly.assertThat(favorite.getSourceStation()).isEqualTo(강남역);
            softly.assertThat(favorite.getTargetStation()).isEqualTo(선릉역);
        });
    }
}
