package nextstep.subway.unit;

import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {

    @Test
    @DisplayName("즐겨찾기 예외 검증 - 출발지와 목적지가 동일한 즐겨찾기")
    void 출발지와_목적지가_동일한_즐겨찾기() {
        Member user = new Member("pakruram12@gmail.com", "asd", 12);
        Favorite favorite = new Favorite(user, 1L ,1L);

        assertThatThrownBy(favorite::validFavorite)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록할 경로가 잘못 되었습니다.");
    }
}
