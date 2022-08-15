package nextstep.subway.unit;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteTest {

    @Test
    @DisplayName("소유자 확인")
    void isOwner() {
        // given
        Long memberId = 1L;
        Favorite favorite = new Favorite(memberId, new Station("강남역"), new Station("역삼역"));

        // when
        boolean result = favorite.isOwner(memberId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("소유자 확인 - 잘못된 소유자")
    void isWrongOwner() {
        // given
        Long memberId = 1L;
        Long invalidMemberId = 2L;
        Favorite favorite = new Favorite(memberId, new Station("강남역"), new Station("역삼역"));

        // when
        boolean result = favorite.isOwner(invalidMemberId);

        // then
        assertThat(result).isFalse();
    }

}
