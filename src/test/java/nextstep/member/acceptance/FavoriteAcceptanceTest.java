package nextstep.member.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given 로그인을 하고
     * When 즐겨찾기 생성을 요청하면
     * Then 즐겨찾기가 생성되고 조회할 수 있다
     */
    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void createFavorite() {

    }

    /**
     * Given 로그인을 하고
     * And 즐겨찾기를 생성하고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제되고 조회할 수 없다
     */
    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {

    }

    /**
     * Given 로그인을 하지 않고
     * When 즐겨찾기 생성을 요청하면
     * Then 에러가 발생한다
     */
    @DisplayName("로그인을 하지 않고 즐겨찾기를 생성하면 에러가 발생한다")
    @Test
    void createFavorite_notLogin_exception() {

    }

    /**
     * Given 로그인을 하지 않고
     * When 즐겨찾기 조회를 요청하면
     * Then 에러가 발생한다
     */
    @DisplayName("로그인을 하지 않고 즐겨찾기를 조회하면 에러가 발생한다")
    @Test
    void selectFavorite_notLogin_exception() {

    }

    /**
     * Given 로그인을 하지 않고
     * When 즐겨찾기 삭제를 요청하면
     * Then 에러가 발생한다
     */
    @DisplayName("로그인을 하지 않고 즐겨찾기를 삭제하면 에러가 발생한다")
    @Test
    void deleteFavorite_notLogin_exception() {

    }

    /**
     * Given 로그인을 하고
     * And 비정상 경로를 생성하고
     * When 즐겨찾기를 생성하면
     * Then 에러가 발생한다
     */
    @DisplayName("비정상 경로를 즐겨찾기로 생성하면 에러가 발생한다")
    @Test
    void createFavorite_abnormalSection_exception() {

    }
}
