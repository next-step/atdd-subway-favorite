package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.testhelper.fixture.StationFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    @DisplayName("즐겨찾기를 등록 할 수 있다")
    void createFavorite1() {
        Favorite favorite = new Favorite(StationFixture.강남역, StationFixture.잠실역, new Member());
    }

    @Test
    @DisplayName("즐겨찾기를 등록 시 출발역과 도착역이 같을 경우 에러 발생")
    void createFavorite2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Favorite(StationFixture.강남역,
                StationFixture.강남역, new Member()));
    }

}
