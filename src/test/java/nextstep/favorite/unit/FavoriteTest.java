package nextstep.favorite.unit;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FavoriteTest {
    @DisplayName("어떤 사용자에 의해 생성된 즐겨찾기인지 확인할 수 있다.")
    @Test
    void checkWhoseFavorite() {
        // given
        Long 사용자1 = 1L;
        Long 사용자2 = 2L;
        Station source = new Station("강남역");
        Station target = new Station("역삼역");
        Favorite favorite = new Favorite(사용자1, source, target);

        // when & then
        assertAll(
                () -> assertThat(favorite.isCreatedBy(사용자1)).isTrue(),
                () -> assertThat(favorite.isCreatedBy(사용자2)).isFalse()
        );
    }
}
