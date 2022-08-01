package nextstep.subway.unit;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteTest {

    @Test
    void 즐겨찾기를_생성한다() {
        // given
        Station 암사역 = new Station("암사역");
        Station 백제고분역 = new Station("백제고분역");
        Long memberId = 1L;

        // when
        Favorite favorite = new Favorite(memberId, 암사역, 백제고분역);

        // then
        assertAll(() -> {
            assertThat(favorite.getSource()).isEqualTo(암사역);
            assertThat(favorite.getTarget()).isEqualTo(백제고분역);
        });
    }
}
