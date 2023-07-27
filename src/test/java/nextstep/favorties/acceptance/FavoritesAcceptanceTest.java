package nextstep.favorties.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 기능")
public class FavoritesAcceptanceTest extends AcceptanceTest {

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void init() {

    }

    /**
     * Given 사용자가 회원가입과 로그인을 하고
     * When 즐겨찾기 생성을 요청하면
     * Then 즐겨찾기가 생성되고
     * When 즐겨찾기를 조회하면
     * Then 생성된 즐겨찾기가 조회되고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제되고
     * When 즐겨찾기를 조회하면
     * Then 즐겨찾기가 조회되지 않는다.
     */
    @DisplayName("즐겨찾기를 생성/조회/삭제한다.")
    @Test
    void saveAndDeleteFavorites() {

    }

    /**
     * Given 사용자가 로그인을 하지 않고
     * When 즐겨찾기 저장을 하면
     * Then 오류가 나고
     * When 즐겨찾기를 조회하면
     * Then 오류가 나고
     * When 즐겨찾기를 삭제하면
     * Then 오류가 난다.
     */
    @DisplayName("로그인하지 않은 사용자가 즐겨찾기를 생성/조회/삭제한다.")
    @Test
    void saveAndDeleteFavorites_noLogin() {

    }

    /**
     * Given 로그인을 한 사용자가
     * When 찾을 수 없는 경로를 즐겨찾기 저장을 하면
     * Then 오류가 난다.
     */
    @DisplayName("로그인하지 않은 사용자가 즐겨찾기를 생성/조회/삭제한다.")
    @Test
    void saveFavorites_wrongPath() {

    }
}
