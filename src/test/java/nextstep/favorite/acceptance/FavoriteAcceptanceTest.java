package nextstep.favorite.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    /*
     * 공통
     * - 권한 없음 -> 401 Unauthorized
     */

    /**
     * # 생성 API
     * ## 요청
     * - POST /favorites
     * - authorization: Bearer
     * - content-type : application/json
     * - body
     * {
     *     "source": "1",
     *     "target": "3"
     * }
     * ---
     * ## 응답
     * - 201 Created
     * - Location: /favorites/1
     */
    @Test
    void create() {

    }

    /**
     * # 조회 API
     * ## 요청
     * - GET /favorites
     * - authorization: Bearer
     * ---
     * ## 응답
     * [
     *     {
     *         "id": 1,
     *         "source": {
     *             "id": 1,
     *             "name": "교대역"
     *         },
     *         "target": {
     *             "id": 3,
     *             "name": "양재역"
     *         }
     *     }
     * ]
     */
    @Test
    void show() {

    }

    /**
     * # 삭제 API
     * ## 요청
     * - DELETE /favorites/{id}
     * - authorization: Bearer
     * ---
     * ## 응답
     * - 204 No Content
     */
    @Test
    void delete() {

    }


}
