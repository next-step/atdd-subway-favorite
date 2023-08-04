package nextstep.subway.acceptance.favorite;

import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given 로그인 돼있는 유저가
     * When 역A에서 역B로 가는 노선을 즐겨찾기에 등록하면
     * Then 즐겨찾기 생성이 성공하고
     * And 즐겨찾기 조회 시, 등록한 즐겨찾기 내역을 확인할 수 있다.
     */
    @DisplayName("즐겨 찾기 등록 성공")
    @Test
    void createFavorite() {

    }

    /**
     * Given 로그인 돼있는 유저가
     * And 역A-역B로 가는 노선을 즐겨찾기에 등록 했을 때
     * When 즐겨찾기 조회 요청을 하면
     * Then 조회에 성공하고
     * And 등록한 즐겨찾기 내역들을 확인할 수 있다.
     */
    @DisplayName("즐겨 찾기 조회 성공")
    @Test
    void getFavorite() {

    }

    /**
     * Given 로그인 돼있는 유저가
     * And 역A-역B로 가는 노선을 즐겨찾기에 등록 했을 때
     * When 해당 즐겨 찾기 id로 즐겨찾기 삭제 요청을 하면
     * Then 삭제 요청이 성공하고
     * And 즐겨찾기 조회 시, 내역을 조회할 수 없다.
     */
    @DisplayName("즐겨 찾기 삭제 성공")
    @Test
    void deleteFavorite() {

    }

    /**
     * Given 로그인 돼있지 않은 유저가
     * When 즐겨 찾기 등록, 조회, 삭제 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("미인증으로 인한 즐겨 찾기 CRD 실패")
    @Test
    void checkAuthentication() {

    }

    /**
     * Given 로그인 돼있는 유저가
     * When 경로 조회 불가한 case로 경로 등록 요청을 하면
     * Then 400 Bad request 응답을 받는다.
     */
    @DisplayName("유효하지 않은 경로로 즐겨찾기 등록 요청")
    @Test
    void createFavoriteFailedByInvalidPath() {

    }
}
