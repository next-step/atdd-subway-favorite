package nextstep.subway.domain;

import nextstep.subway.applicaion.exceptions.CanNotDeleteFavoriteException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    @Test
    void 즐겨찾기_삭제_시_본인의_즐겨찾기가_아닌_경우_예외가_발생한다() {
        // given
        var source = new Station();
        var target = new Station();
        var 즐겨찾기_추가한_회원_아이디 = 1L;
        var 다른_회원_아이디 = 2L;

        Favorite favorite = new Favorite(즐겨찾기_추가한_회원_아이디, source, target);

        // when then
        assertThatThrownBy(() -> favorite.delete(다른_회원_아이디)).isInstanceOf(CanNotDeleteFavoriteException.class);
    }
}
