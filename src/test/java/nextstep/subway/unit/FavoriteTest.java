package nextstep.subway.unit;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.InvalidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FavoriteTest {
    private static final long MEMBER_ID = 2L;
    private static final long ID = 1L;
    private Station source = new Station(3L, "강남역");
    private Station target = new Station(4L, "강남역");

    @Test
    @DisplayName("즐겨 찾기의 식별자, 사용자의 식별자, 그리고 출발역과 도착역의 식별자를 가집니다.")
    void createFavorite() {
        //when & then
        assertDoesNotThrow(() -> new Favorite(ID, MEMBER_ID, source, target));
    }

    @Test
    @DisplayName("출발역과 도착역의 식별자는 같을 수 없습니다.")
    void favorite_source_target_not_same() {
        //when & then
        assertThatThrownBy(
            () -> new Favorite(ID, MEMBER_ID, source, source)
        ).isInstanceOf(InvalidDataException.class);
    }
}
