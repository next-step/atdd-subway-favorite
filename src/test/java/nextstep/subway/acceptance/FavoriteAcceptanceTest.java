package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.member.aceeptance.MemberSteps.*;
import static nextstep.subway.acceptance.FavoriteSteps.지하철_즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.지하철_즐겨찾기_생성_응답됨;
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
    @DisplayName("즐겨찾기 추가")
    @Test
    void createFavorite() {
        ExtractableResponse<Response> 즐겨찾기_요청 = 지하철_즐겨찾기_생성_요청(교대역, 양재역, 로그인_토큰);

        지하철_즐겨찾기_생성_응답됨(즐겨찾기_요청);
    }
}
