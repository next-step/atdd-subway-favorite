package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest {

    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 모두 조회했을때
     * Then 생성한 즐겨찾기가 조회목록에 포함되어 있다.
     */
    @DisplayName("즐겨찾기를 조회한다")
    @Test
    void getFavorites() {

    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 해당 즐겨찾기를 삭제했을때
     * When 즐겨찾기를 모두 조회하면
     * Then 삭제한 즐겨찾기를 찾을 수 없다.
     */
    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorites() {

    }

    /**
     * Given 비로그인 상태에서,
     * When 즐겨찾기 생성 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("비로그인 상태에서 즐겨찾기를 생성한다")
    @Test
    void saveFavorites_fail_not_login() {

    }

    /**
     * Given 비로그인 상태에서,
     * When 즐겨찾기 조회 생성 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("비로그인 상태에서 즐겨찾기를 조회한다")
    @Test
    void getFavorites_fail_not_login() {

    }

    /**
     * Given 비로그인 상태에서,
     * When 즐겨찾기 조회 삭제 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("비로그인 상태에서 즐겨찾기를 삭제한다")
    @Test
    void deleteFavorites_fail_not_login() {

    }
}
