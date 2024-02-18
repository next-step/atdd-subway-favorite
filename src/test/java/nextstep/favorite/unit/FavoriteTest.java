package nextstep.favorite.unit;

import nextstep.favorite.domain.Favorite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    @DisplayName("Source와 Target이 동일한 즐겨찾기는 생성 할 수 없다. ")
    @Test
    void create_invalid() {
        assertThatThrownBy(() -> { new Favorite(null, 2L, 2L); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("source와 target은 같을 수 없습니다.");
    }
}