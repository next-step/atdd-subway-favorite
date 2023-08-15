package nextstep.subway.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.auth.AuthSteps.로그인_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "password";
    private final int AGE = 20;
    private String accessToken;

    /**
     * Given 3개의 역을 생성하고
     * Given 회원가입을 하고
     * Given 로그인을 하고
     */
    @BeforeEach
    void setUpFavoriteAcceptanceTest() {

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_요청(EMAIL, PASSWORD);
    }

    /**
     * When 즐겨찾기를 생성하면
     * Then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {

    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 조회하면
     * Then 즐겨찾기가 조회된다.
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {

    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {

    }
}