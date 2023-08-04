package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;

@DisplayName("즐겨찾기 기능 인수테스트")
public class FavoriteAcceptanceTest {

    /*
    Given 즐겨찾기를 추가하면
    When 즐겨찾기 목록을 조회했을 때
    Then 즐겨찾기 목록에 추가한 즐겨찾기가 조회된다
     */

    /*
    Given 권한이 없는 유저가
    When 즐겨찾기를 추가하면
    Then 401 Unauthorized를 응답받는다
     */

    /*
    Given 권한이 없는 유저가
    When 즐겨찾기를 조회하면
    Then 401 Unauthorized를 응답받는다
    * */

    /*
    Given 즐겨찾기 목록이 있고
    When 즐겨찾기를 삭제하면
    Then 즐겨찾기 목록에서 삭제한 즐겨찾기가 조회되지 않는다
     */

    /*
    Given 즐겨찾기를 추가한 후
    And 권한이 없는 유저가
    When 즐겨찾기를 삭제하면
    Then 401 Unauthorized를 응답받는다
    * */

    /*
    When 없는 즐겨찾기 id에 대해 즐겨찾기를 삭제 요청하면
    Then 400 Bad Request를 응답받는다
    * */
}
