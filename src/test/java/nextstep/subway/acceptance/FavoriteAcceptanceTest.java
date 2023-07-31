package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.깃헙_AccessToken으로_즐겨_찾기_생성한다;
import static nextstep.subway.acceptance.FavoriteSteps.깃헙_로그인하고_AccessToken_받아온다;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨_찾기_생성_검증;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨 찾기 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * When 즐겨 찾기를 생성하면
     * Then 응답코드는 201로 받고 Location 헤더에 주소를 보내준다
     */
    @DisplayName("즐겨 찾기 생성")
    @Test
    void createFavorite() {
        // when
        String accessToken = 깃헙_로그인하고_AccessToken_받아온다();
        var response = 깃헙_AccessToken으로_즐겨_찾기_생성한다(accessToken);

        // then
        즐겨_찾기_생성_검증(response);
    }


}
