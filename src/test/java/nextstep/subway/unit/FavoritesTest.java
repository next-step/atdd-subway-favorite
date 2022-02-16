package nextstep.subway.unit;


import nextstep.member.domain.Member;
import nextstep.subway.domain.Favorites;
import nextstep.subway.ui.exception.FavoritesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoritesTest {

    @DisplayName("본인의 즐겨찾기가 아닌 경우")
    @Test
    void exception_canDeleteFavorites() {
        // given
        Long loginId = 1L;
        Favorites favorites = new Favorites(2L, new Member(2L, null, null, null), null, null);

        // when
        assertThatThrownBy(() -> favorites.canDeleteFavorites(loginId))
                .isInstanceOf(FavoritesException.class)
                // then
                .hasMessage("자신의 즐겨찾기만 삭제할 수 있습니다.");

    }
}
