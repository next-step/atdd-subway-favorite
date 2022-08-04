package nextstep.subway.unit;

import nextstep.favorite.domain.Favorite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련")
public class FavoriteTest {

    @DisplayName("소유자가 아닌 경우 확인")
    @Test
    void isNotOwner() {
        // given
        Favorite favorite = new Favorite(1L, null, null);

        // when
        boolean owner = favorite.isNotOwner(123L);

        // then
        assertThat(owner).isTrue();
    }

    @DisplayName("소유자 확인")
    @Test
    void isOwner() {
        // given
        Favorite favorite = new Favorite(1L, null, null);

        // when
        boolean owner = favorite.isNotOwner(1L);

        // then
        assertThat(owner).isFalse();
    }
}
