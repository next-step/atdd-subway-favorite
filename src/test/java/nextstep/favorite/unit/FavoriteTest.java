package nextstep.favorite.unit;

import nextstep.favorite.domain.Favorite;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteTest {

    /**
    * when 즐겨찾기 생성자와 즐겨찾기 삭제 요청자가 다르면
    * then IllegalArgumentException을 발생시킨다.
    */

    @DisplayName("즐겨찾기 생성자와 즐겨찾기 삭제요청자가 다르면 에러를 발생시킨다.")
    @Test
    void validateCreatorEqualRequester() {
        Favorite favorite = new Favorite(1L, 1L, 1L, 2L);
        Assertions.assertThatThrownBy(() -> favorite.validate(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 즐겨찾기 생성자가 아닙니다.");
    }
}
