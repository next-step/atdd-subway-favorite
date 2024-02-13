package nextstep.favorite.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철역이 등록되어 있다.
     * And 노선이 등록되어 있다.
     * When 즐겨찾기를 등록한다.
     * Then HTTP 코드 201을 리턴한다.
     */
    void createFavorite() {

    }

    /**
     * Given 지하철역이 등록되어 있다.
     * And 노선이 등록되어 있다.
     * When JWT 토큰 없이, 즐겨찾기를 등록한다.
     * Then 권한이 없으므로, HTTP 코드 401을 리턴한다.
     */
    void createFavorite_invalid_jwt() {

    }


    /**
     * Given 지하철역이 등록되어 있다.
     * And 노선이 등록되어 있다.
     * When 비정상 경로를 즐겨찾기로 등록한다.
     * Then 잘못된 요청이므로, HTTP 코드 400 리턴한다.
     */
    void createFavorite_invalid_path() {

    }
}