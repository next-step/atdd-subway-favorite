package nextstep.core.favorite.acceptance;

import nextstep.common.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    @Nested
    class 즐겨찾기_추가 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성한다.
             * When  즐겨찾기를 추가하면
             * Then  해당 회원의 즐겨찾기 목록에 추가된다.
             */
        }
        @Nested
        class 실패 {
            @Nested
            class 회원_관련 {
                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     회원 정보를 전달하지 않은 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */

                /**
                 * When  즐겨찾기를 추가할 때,
                 * When     비정상적인 회원 정보를 전달한 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
            }

            @Nested
            class 경로_관련 {
                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     존재하지 않는 출발역일 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     존재하지 않는 도착역일 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     출발역과 도착역이 동일할 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */

                /**
                 * Given 회원을 생성한다.
                 * When  즐겨찾기를 추가할 때,
                 * When     출발역과 도착역이 연결되지 않았을 경우
                 * Then  해당 회원의 즐겨찾기 목록에 추가할 수 없다.
                 */
            }
        }
    }

    @Nested
    class 즐겨찾기_조회 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청하면
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 있다.
             */
        }
        
        @Nested
        class 실패 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청할 때
             * When     회원 정보를 전달하지 않은 경우
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 있다.
             */

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  즐겨찾기 목록을 요청할 때
             * When     비정상적인 회원 정보를 전달한 경우
             * Then  추가된 즐겨찾기를 즐겨찾기 목록에서 확인할 수 있다.
             */
        }
    }

    @Nested
    class 즐겨찾기_삭제 {
        @Nested
        class 성공 {
            /**
              * Given 회원을 생성하고, 즐겨찾기를 추가한다.
              * When  추가한 즐겨찾기를 삭제할 경우
              * Then  즐겨찾기 목록에서 삭제된다.
              */
        }
        @Nested
        class 실패 {
            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 다른 사용자의 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패할 수 없다.
             */

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기가 아닌 존재하지 않는 즐겨찾기를 삭제할 경우
             * Then  즐겨찾기 삭제에 실패할 수 없다.
             */

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기를 삭제할 때
             * When     회원 정보를 전달하지 않은 경우
             * Then  즐겨찾기 삭제에 실패할 수 없다.
             */

            /**
             * Given 회원을 생성하고, 즐겨찾기를 추가한다.
             * When  추가한 즐겨찾기를 삭제할 때
             * When     비정상적인 회원 정보를 전달한 경우
             * Then  즐겨찾기 삭제에 실패할 수 없다.
             */
        }
    }
}