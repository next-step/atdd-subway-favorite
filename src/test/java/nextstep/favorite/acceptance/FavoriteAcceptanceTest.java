package nextstep.favorite.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기가 생성된다.
     */
    @Test
    void 즐겨_찾기_생성() {
        // when
        // then
    }

    /**
     * given: 경로가 존재하지 않는 두 역이 주어 졌을 때
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기 생성에 실패한다.
     */
    @Test
    void 즐겨_찾기_생성_시_비_정상_경로이면_등록이_불가하다() {
        // when
        // then
    }

    /**
     * given: 유효하지 않은 bearer token에 대해서
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기 생성에 실패한다.
     */
    @Test
    void  즐겨_찾기_요청_시_bearer_token이_유효하지_않으면_안된다() {

    }
}