package nextstep.favorite.acceptance;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;

import io.restassured.RestAssured;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * When 즐겨찾기를 생성을 요청하면
     * Then 즐겨찾기 조회시 내가 생성한 즐겨 찾기를 찾을 수 있다.
     */
    @Test
    void 즐겨찾기_생성() {
        // when

        // then
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 조회를 하면
     * Then 내가 생성한 즐겨찾기를 확인 할 수 있다.
     */
    @Test
    void 즐겨찾기_조회() {
        // given

        // when

        // then
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 삭제를 하면
     * Then 해당 즐겨찾기는 삭제가 된다.
     */
    @Test
    void 즐겨찾기_삭제() {
        // given

        // when

        // then
    }
}