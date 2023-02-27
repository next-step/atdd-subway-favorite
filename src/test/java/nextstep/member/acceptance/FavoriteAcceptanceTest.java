package nextstep.member.acceptance;

import nextstep.common.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.common.constants.ErrorConstant.INVALID_AUTHENTICATION_INFO;
import static nextstep.member.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private String 관리자;

    private Long 강남역;
    private Long 역삼역;
    private Long 선릉역;

    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청("역삼역").jsonPath().getLong("id");
        선릉역 = 지하철역_생성_요청("선릉역").jsonPath().getLong("id");

        관리자 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * When 비로그인 상태에서 경로를 즐겨찾기 등록 요청하면
     * Then 예외가 발생한다.
     */
    @Test
    @DisplayName("비로그인 상태에서 경로 즐겨찾기 등록")
    void createFavorite_notLogin() {
        // when
        var response = 비로그인_경로_즐겨찾기_등록_요청(강남역, 선릉역);

        // then
        권한없는_요청_검증(response, INVALID_AUTHENTICATION_INFO);
    }

    /**
     * When 유효하지 않은 토큰으로 경로를 즐겨찾기 등록 요청하면
     * Then 예외가 발생한다.
     */
    @Test
    @DisplayName("유효하지 않은 토큰으로 경로 즐겨찾기 등록")
    void createFavorite_invalidToken() {
        // when
        var response = 경로_즐겨찾기_등록_요청("invalidToken", 강남역, 선릉역);

        // then
        권한없는_요청_검증(response, INVALID_AUTHENTICATION_INFO);
    }

    /**
     * When 경로를 즐겨찾기 등록 요청하면
     * Then 즐겨찾기가 등록된다.
     */
    @Test
    @DisplayName("경로 즐겨찾기 등록")
    void createFavorite() {
        // when
        var response = 경로_즐겨찾기_등록_요청(관리자, 강남역, 선릉역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * When 비로그인 상태에서 즐겨찾기 조회를 요청하면
     * Then 예외가 발생한다.
     */

    /**
     * When 유효하지 않은 토큰으로 즐겨찾기 조회를 요청하면
     * Then 예외가 발생한다.
     */

    /**
     * Given 즐겨찾기를 등록하고
     * When 즐겨찾기 조회를 요청하면
     * Then 즐겨찾기 목록이 조회된다.
     */

    /**
     * When 비로그인 상태에서 즐겨찾기 삭제를 요청하면
     * Then 예외가 발생한다.
     */

    /**
     * When 유효하지 않은 토큰으로 즐겨찾기 삭제를 요청하면
     * Then 예외가 발생한다.
     */

    /**
     * Given 즐겨찾기를 등록하고
     * When 등록한 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 목록에서 삭제된다.
     */
}
