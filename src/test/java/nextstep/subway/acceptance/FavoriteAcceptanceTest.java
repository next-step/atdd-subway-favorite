package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given Token 인증을 통해 로그인한다.
     * When 경로(출발역, 도착역)을 즐겨찾기로 추가하면
     * Then 즐겨찾기 목록 조회 시 추가 된 경로를 확인할 수 있다.
     */
    @DisplayName("즐겨찾기 경로를 추가한다.")
    @Test
    void addFavoritePath() {

    }

    /**
     * Given 로그인하지 않고
     * When 경로(출발역, 도착역)을 즐겨찾기로 추가하면
     * Then 401 Unauthorized Exception 을 만난다.
     */
    @DisplayName("비로그인 회원이 즐겨찾기 경로를 추가하면 예외")
    @Test
    void anonymousUserAddFavoritePathException() {

    }

    /**
     * Given Token 인증을 통해 로그인한다.
     * Given 2개의 경로(출발역, 도착역)을 즐겨찾기로 추가하고
     * When 나의 즐겨찾기 목록을 조회하면
     * Then 즐겨찾기 목록에 2개의 경로를 확인할 수 있다.
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {

    }

    /**
     * Given Token 인증을 통해 로그인한다.
     * Given 경로(출발역, 도착역)을 즐겨찾기로 추가하고
     * When 즐겨찾기를 삭제하면
     * Then 해당 즐겨찾기 정보는 삭제된다.
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {

    }
}
