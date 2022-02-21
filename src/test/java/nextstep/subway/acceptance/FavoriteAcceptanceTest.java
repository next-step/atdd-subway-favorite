package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.member.aceeptance.MemberSteps.*;
import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.FavoriteSteps.로그인_지하철_즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private Long 교대역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 삼호선;
    private String 로그인_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_요청(EMAIL, PASSWORD, 30);

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 양재역, 2).jsonPath().getLong("id");
        로그인_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    /**
     * When 즐겨찾기 요청하면
     * Then 즐겨찾기 추가가 성공한다.
     */
    @DisplayName("로그인 후 즐겨찾기 추가")
    @Test
    void loginCreateFavorite() {
        ExtractableResponse<Response> 즐겨찾기_생성_요청 = 로그인_지하철_즐겨찾기_생성_요청(교대역, 양재역, 로그인_토큰);

        지하철_즐겨찾기_생성_응답됨(즐겨찾기_생성_요청);
    }

    /**
     * When 로그인을 하지 않고 즐겨찾기를 추가하면
     * Then 에러가 발생한다.
     */
    @DisplayName("비로그인 즐겨찾기 추가")
    @Test
    void notLoginCreateFavorite() {
        ExtractableResponse<Response> 즐겨찾기_요청 = 비로그인_지하철_즐겨찾기_생성_요청(교대역, 양재역);

        비로그인_지하철_즐겨찾기_생성_응답됨(즐겨찾기_요청);
    }

    /**
     * Given 즐겨찾기를 추가하고
     * When 중복 즐겨찾기를 추가하면
     * Then 에러가 발생한다.
     */
    @DisplayName("중복 즐겨찾기 추가")
    @Test
    void duplicateFavorite() {
        로그인_지하철_즐겨찾기_생성_요청(교대역, 양재역, 로그인_토큰);

        ExtractableResponse<Response> 중복_즐겨찾기_요청 = 로그인_지하철_즐겨찾기_생성_요청(교대역, 양재역, 로그인_토큰);

        즐겨찾기_중복_생성_응답됨(중복_즐겨찾기_요청);
    }

    /**
     * When 즐겨찾기 삭제를 요청하면
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        ExtractableResponse<Response> 즐겨찾기_생성_요청 = 로그인_지하철_즐겨찾기_생성_요청(교대역, 양재역, 로그인_토큰);

        ExtractableResponse<Response> 삭제_요청 = 로그인_지하철_즐겨찾기_삭제_요청(즐겨찾기_생성_요청.header("location"), 로그인_토큰);

        즐겨찾기_삭제_응답됨(삭제_요청);
    }

    /**
     * When 즐겨 찾기 목록 조회 요청하면
     * Then 즐겨찾기 목록이 반환된다.
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        로그인_지하철_즐겨찾기_생성_요청(교대역, 양재역, 로그인_토큰);

        ExtractableResponse<Response> 로그인_지하철_즐겨찾기_목록_요청 = 로그인_지하철_즐겨찾기_목록_요청(로그인_토큰);

        즐겨찾기_목록_응답됨(로그인_지하철_즐겨찾기_목록_요청, 교대역, 양재역);
    }

    /**
     * When 즐겨찾기 생성 요청
     * Then 즐겨찾기 생성 됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨 찾기 목록 조회
     * When 즐겨 찾기 목록 삭제 요청
     * Then 즐겨 찾기 삭제 됨
     */
    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorites() {
        ExtractableResponse<Response> 즐겨찾기_생성_요청 = 로그인_지하철_즐겨찾기_생성_요청(교대역, 양재역, 로그인_토큰);
        지하철_즐겨찾기_생성_응답됨(즐겨찾기_생성_요청);

        ExtractableResponse<Response> 로그인_지하철_즐겨찾기_목록_요청 = 로그인_지하철_즐겨찾기_목록_요청(로그인_토큰);
        즐겨찾기_목록_응답됨(로그인_지하철_즐겨찾기_목록_요청, 교대역, 양재역);


        ExtractableResponse<Response> 삭제_요청 = 로그인_지하철_즐겨찾기_삭제_요청(즐겨찾기_생성_요청.header("location"), 로그인_토큰);
        즐겨찾기_삭제_응답됨(삭제_요청);
    }
}
