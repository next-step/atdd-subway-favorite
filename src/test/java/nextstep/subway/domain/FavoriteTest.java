package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FavoriteTest {


    public static final long MEMBER_ID = 2L;
    public static final long ID = 1L;

    @Test
    @DisplayName("즐겨 찾기의 식별자, 사용자의 식별자, 그리고 출발역과 도착역의 식별자를 가집니다.")
    void createFavorite() {
        // given
        long sourceId = 3L;
        long targetId = 4L;

        //when & then
        assertDoesNotThrow(() -> new Favorite(ID, MEMBER_ID, sourceId, targetId));
    }

    @Test
    @DisplayName("출발역과 도착역의 식별자는 같을 수 없습니다.")
    void favorite_source_target_not_same() {
        // given
        long sourceId = 3L;
        long targetId = 3L;

        //when & then
        assertThatThrownBy(
            () -> new Favorite(ID, MEMBER_ID, sourceId, targetId)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
