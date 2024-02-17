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
     * given: 존재하지 않는 역 id에 대해서
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기 생성에 실패한다.
     */
    @Test
    void  즐겨_찾기_요청_시_source와_target역_모두_존재해야_한다() {
        // when
        // then
    }

    /**
     * given: source와 target이 같은 역 id에 대해서
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기 생성에 실패한다.
     */
    @Test
    void 즐겨_찾기_요청_시_source와_target역이_같으면_안된다() {
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