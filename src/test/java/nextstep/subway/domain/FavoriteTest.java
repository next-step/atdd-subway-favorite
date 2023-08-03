package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    @DisplayName("즐겨찾기 삭제시 소유하는 유저가 아니라면 exception")
    void validDelete() {

        //given
        Station source = mock(Station.class);
        Station target = mock(Station.class);
        Long memberId = 1L;

        Favorite favorite = Favorite.of(source, target, memberId);

        //then
        assertThatThrownBy(
            () -> favorite.validDelete(2L)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}