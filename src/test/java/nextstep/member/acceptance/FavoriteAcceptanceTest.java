package nextstep.member.acceptance;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.study.AuthSteps.로그인_후_엑세스토큰_획득;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 경로를 즐겨찾기 한다면
     * Then 경로 찾기가 등록된다
     */
    @DisplayName("즐겨 찾기 생성")
    @Test
    void createFavorite() {

    }

    /**
     * When 로그인 없이 경로를 즐겨찾기 한다면
     * Then 401 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 생성 - 권한 없음")
    @Test
    void createFavoriteThrowUnAuthorizeException() {

    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 잘못된 경로를 즐겨찾기 한다면
     * Then 403 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 생성 - 잘못된 경로")
    @Test
    void createFavoriteThrowInvalidPath() {

    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * And 경로를 즐겨찾기 하고
     * When 즐겨찾기를 조회하면
     * Then 등록한 즐겨찾기를 반환한다
     */
    @DisplayName("즐겨 찾기 조회")
    @Test
    void getFavorites() {

    }

    /**
     * When 로그인 없이 즐겨찾기를 조회하면
     * Then 401 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 조회 - 권한 없음")
    @Test
    void getFavoritesThrowUnAuthorizeException() {

    }


    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * And 경로를 즐겨찾기 하고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨 찾기 삭제")
    @Test
    void deleteFavorite() {

    }


    /**
     * When 로그인 없이 즐겨찾기를 삭제하면
     * Then 401 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 삭제 - 권한 없음")
    @Test
    void deleteFavoriteThrowUnAuthorizeException() {

    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * And 경로를 즐겨찾기 하고
     * When 잘못된 즐겨찾기를 삭제하면
     * Then 403 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 삭제 - 잘못된 요청")
    @Test
    void deleteFavoriteThrowInvalidParameterException() {

    }
}
