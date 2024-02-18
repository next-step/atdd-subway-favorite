package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.testhelper.fixture.StationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    @DisplayName("즐겨찾기를 등록 할 수 있다")
    void createFavorite() {
        Favorite favorite = new Favorite(StationFixture.강남역, StationFixture.잠실역, new Member());
    }

}
