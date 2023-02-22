package nextstep.subway.unit;

import nextstep.common.exception.AuthorityException;
import nextstep.subway.domain.Favorite;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {

    @Test
    void 즐겨찾기를_생성한다() {
        //given
        Long 사용자 = 1L;
        Long 강남역 = 1L;
        Long 정자역 = 2L;

        //when
        Favorite favorite = new Favorite(사용자, 강남역, 정자역);

        //then
        assertThat(favorite).isNotNull();
    }

    @Test
    void 즐겨찾기를_등록한_사용자가_리소스_권한을_가진다() {
        //given
        Long 사용자 = 1L;
        Long 강남역 = 1L;
        Long 정자역 = 2L;
        Favorite favorite = new Favorite(사용자, 강남역, 정자역);

        //when
        boolean isOwner = favorite.isOwner(사용자);

        //then
        assertThat(isOwner).isTrue();
    }

    @Test
    void 즐겨찾기_리소스_권한이_없으면_AuthorityException이_발생한다() {
        //given
        Long 사용자 = 1L;
        Long 강남역 = 1L;
        Long 정자역 = 2L;
        Favorite favorite = new Favorite(사용자, 강남역, 정자역);

        //when
        //then
        assertThatThrownBy(() -> favorite.isOwner(사용자 + 1L)).isInstanceOf(AuthorityException.class);
    }
}
