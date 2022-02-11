package nextstep.subway.unit;

import nextstep.subway.domain.Favorite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteTest {
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // given
        Long memberId = 1L;
        Long sourceId = 1L;
        Long targetId = 2L;

        // when
        Favorite favorite = new Favorite(memberId, sourceId, targetId);

        // then
        assertThat(favorite.getMemberId()).isEqualTo(memberId);
        assertThat(favorite.getSourceId()).isEqualTo(sourceId);
        assertThat(favorite.getTargetId()).isEqualTo(targetId);
    }
}
