package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.favorite.acceptance.FavoriteSteps.권한없음;
import static nextstep.favorite.acceptance.FavoriteSteps.내_즐겨찾기_목록만_조회됨;
import static nextstep.favorite.acceptance.FavoriteSteps.다른_사용자의_즐겨찾기_삭제_실패;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_목록_조회됨;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제됨;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_실패함;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_응답_식별자;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성됨;
import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청_성공;
import static nextstep.subway.acceptance.LineSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "member@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 29;

    private Long 고속터미널역;
    private Long 강남구청역;
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 칠호선;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private String 유효한_토큰;
    private String 유효하지_않은_토큰;

    /**
     * 고속터미널역  --- *7호선* ---  강남구청역
     *
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        고속터미널역 = 지하철역_생성_요청("고속터미널역").jsonPath().getLong("id");
        강남구청역 = 지하철역_생성_요청("강남구청역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        칠호선 = 지하철_노선_생성_요청("7호선", "dark green", 고속터미널역, 강남구청역, 5);
        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));

        유효한_토큰 = 베어러_인증_로그인_요청_성공(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        유효하지_않은_토큰 = "invalid token";
    }

    /**
     * Given 사용자가 로그인 후, 노선과 역, 구간을 등록하고
     * When 출발역, 도착역, 2가지 역을 선택하여 즐겨찾기 생성 요청하면
     * Then 두 역에 대한 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_요청_응답 = 즐겨찾기_생성_요청(유효한_토큰, 교대역, 양재역);

        // then
        즐겨찾기_생성됨(즐겨찾기_생성_요청_응답);
    }

    /**
     * Given 사용자가 로그인 후, 노선과 역, 구간을 등록하고
     * When 출발역, 도착역이 연결되지 않은 2가지 역을 선택하여 즐겨찾기 생성 요청하면
     * Then 두 역에 대한 즐겨찾기를 생성할 수 없다.
     */
    @DisplayName("즐겨찾기 생성 예외")
    @Test
    void createFavoriteExceptionNotConnect() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_요청_응답 = 즐겨찾기_생성_요청(유효한_토큰, 고속터미널역, 양재역);

        // then
        즐겨찾기_생성_실패함(즐겨찾기_생성_요청_응답);
    }

    /**
     * Given 사용자가 로그인 후, 노선과 역, 구간을 등록하고, 두 역에 대한 즐겨찾기를 여러번 등록하고
     * When 내 즐겨찾기 목록을 조회하면
     * Then 즐겨찾기 목록을 조회할 수 있다.
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        // given
        Long 교대_양재_즐겨찾기 = 즐겨찾기_생성_응답_식별자(즐겨찾기_생성_요청(유효한_토큰, 교대역, 양재역));
        Long 강남_남부터미널_즐겨찾기 = 즐겨찾기_생성_응답_식별자(즐겨찾기_생성_요청(유효한_토큰, 강남역, 남부터미널역));

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(유효한_토큰);

        // then
        즐겨찾기_목록_조회됨(교대_양재_즐겨찾기, 강남_남부터미널_즐겨찾기, 즐겨찾기_목록_조회_응답);
    }

    /**
     * Given 사용자가 로그인 후, 노선과 역, 구간을 등록하고, 두 역에 대한 즐겨찾기를 생성하고
     * When 등록한 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        Long 교대_양재_즐겨찾기 = 즐겨찾기_생성_응답_식별자(즐겨찾기_생성_요청(유효한_토큰, 교대역, 양재역));;

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = 즐겨찾기_삭제_요청(유효한_토큰, 교대_양재_즐겨찾기);

        // then
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_응답 = 즐겨찾기_목록_조회_요청(유효한_토큰);
        즐겨찾기_삭제됨(교대_양재_즐겨찾기, 즐겨찾기_목록_조회_요청_응답, 즐겨찾기_삭제_요청_응답);
    }

    /**
     * Given 두 사용자가 로그인 후 서로 다른 즐겨찾기를 등록한 후
     * When 한 사용자의 즐겨찾기 목록을 조회하면
     * Then 내가 등록한 즐겨찾기만 조회 가능하고, 다른 사용자의 즐겨찾기 목록은 조회되지 않는다.
     */
    @DisplayName("두 사용자 중 자신의 즐겨찾기 목록만 조회")
    @Test
    void getFavoritesMine() {
        // given
        String 다른_사용자의_유효한_토큰 = 베어러_인증_로그인_요청_성공("admin@email.com", "password").jsonPath().getString("accessToken");

        Long 내_교대_양재_즐겨찾기 = 즐겨찾기_생성_응답_식별자(즐겨찾기_생성_요청(유효한_토큰, 교대역, 양재역));
        Long 다른_사용자의_강남_남부터미널_즐겨찾기 = 즐겨찾기_생성_응답_식별자(즐겨찾기_생성_요청(다른_사용자의_유효한_토큰, 강남역, 남부터미널역));

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(유효한_토큰);

        // then
        내_즐겨찾기_목록만_조회됨(내_교대_양재_즐겨찾기, 다른_사용자의_강남_남부터미널_즐겨찾기, 즐겨찾기_목록_조회_응답);
    }

    /**
     * Given 두 사용자가 로그인 후 서로 다른 즐겨찾기를 등록한 후
     * When 한 사용자가 다른 사용자의 즐겨찾기를 삭제 요쳥하면
     * Then 다른 사용자의 즐겨찾기를 삭제할 수 없다.
     */
    @DisplayName("다른 사용자의 즐겨찾기 삭제를 할 수 없다.")
    @Test
    void doNotDeleteOtherMemberFavorite() {
        // given
        String 다른_사용자의_유효한_토큰 = 베어러_인증_로그인_요청_성공("admin@email.com", "password").jsonPath().getString("accessToken");

        즐겨찾기_생성_요청(유효한_토큰, 교대역, 양재역);
        Long 다른_사용자의_강남_남부터미널_즐겨찾기 = 즐겨찾기_생성_응답_식별자(즐겨찾기_생성_요청(다른_사용자의_유효한_토큰, 강남역, 남부터미널역));

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(유효한_토큰, 다른_사용자의_강남_남부터미널_즐겨찾기);

        // then
        다른_사용자의_즐겨찾기_삭제_실패(즐겨찾기_삭제_응답);
    }

    /**
     * When 유효하지 않은 사용자가 즐겨찾기를 생성, 조회, 삭제 요청할 경우
     * Then 모두 예외가 발생한다.
     */
    @DisplayName("유효하지 않은 사용자의 즐겨찾기 관리 기능 예외")
    @Test
    void favoriteFunctionExceptionNotLogin() {
        // when & then
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(유효하지_않은_토큰, 교대역, 양재역);
        권한없음(createResponse);

        // when & then
        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청(유효하지_않은_토큰);
        권한없음(getResponse);

        // when & then
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(유효하지_않은_토큰, 1L);
        권한없음(deleteResponse);
    }
}
