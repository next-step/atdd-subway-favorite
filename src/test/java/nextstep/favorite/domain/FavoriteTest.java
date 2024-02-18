package nextstep.favorite.domain;

import nextstep.common.fixture.StationFactory;
import nextstep.favorite.exception.FavoriteCreationException;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FavoriteTest {

    private Long memberId;
    private Station 강남역;
    private Station 선릉역;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        강남역 = StationFactory.createStation(1L, "강남역");
        선릉역 = StationFactory.createStation(2L, "선릉역");
    }

    @Test
    @DisplayName("Favorite 객체를 생성할 수 있다.")
    void favoriteCreateTest() {
        final Favorite favorite = new Favorite(memberId, 강남역, 선릉역);

        assertSoftly(softly -> {
            softly.assertThat(favorite.getMemberId()).isEqualTo(memberId);
            softly.assertThat(favorite.getSourceStation()).isEqualTo(강남역);
            softly.assertThat(favorite.getTargetStation()).isEqualTo(선릉역);
        });
    }

    @Test
    @DisplayName("Favorite 객체 생성시 파라미터는 null 일 수 없다.")
    void favoriteCreationValidateTest() {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Favorite(null, 강남역, 선릉역)).isInstanceOf(FavoriteCreationException.class);
            softly.assertThatThrownBy(() -> new Favorite(memberId, null, 선릉역)).isInstanceOf(FavoriteCreationException.class);
            softly.assertThatThrownBy(() -> new Favorite(memberId, 강남역, null)).isInstanceOf(FavoriteCreationException.class);
        });
    }
}
