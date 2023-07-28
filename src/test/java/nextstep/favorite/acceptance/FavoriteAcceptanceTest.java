package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.LineStep;
import nextstep.subway.acceptance.step.SectionStep;
import nextstep.subway.acceptance.step.StationStep;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.auth.acceptance.step.TokenSteps.일반_로그인_요청;
import static nextstep.auth.acceptance.step.TokenSteps.토큰_추출;
import static nextstep.favorite.acceptance.step.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.member.acceptance.step.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = StationStep.지하철역을_생성한다("교대역").jsonPath().getLong("id");
        강남역 = StationStep.지하철역을_생성한다("강남역").jsonPath().getLong("id");
        양재역 = StationStep.지하철역을_생성한다("양재역").jsonPath().getLong("id");
        남부터미널역 = StationStep.지하철역을_생성한다("남부터미널역").jsonPath().getLong("id");

        이호선 = LineStep.지하철_노선을_생성한다("2호선", "green", 교대역, 강남역, 10).jsonPath().getLong("id");
        신분당선 = LineStep.지하철_노선을_생성한다("신분당선", "red", 강남역, 양재역, 10).jsonPath().getLong("id");
        삼호선 = LineStep.지하철_노선을_생성한다("3호선", "orange", 교대역, 남부터미널역, 2).jsonPath().getLong("id");

        SectionStep.지하철_노선_구간을_등록한다(삼호선, 남부터미널역, 양재역, 3);
    }

    /*
     * 공통
     * - 권한 없음 -> 401 Unauthorized
     */

    /**
     * # 생성 API
     * ## 요청
     * - POST /favorites
     * - authorization: Bearer
     * - content-type : application/json
     * - body
     * {
     *     "source": "1",
     *     "target": "3"
     * }
     * ---
     * ## 응답
     * - 201 Created
     * - Location: /favorites/1
     * ---
     * ## 시나리오
     * Given : 지하철역과 노선을 생성하고 (@BeforeEach)
     * And : 회원을 생성하고
     * And : 토큰을 발급받은 후
     * When : 출발역 id와 도착역 id를 전송하면
     * Then : 즐겨찾기가 생성(등록)된다.
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 토큰_추출(일반_로그인_요청(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //TODO: 즐겨찾기 조회 요청 구현후 작성
    }

    /**
     * # 조회 API
     * ## 요청
     * - GET /favorites
     * - authorization: Bearer
     * ---
     * ## 응답
     * [
     *     {
     *         "id": 1,
     *         "source": {
     *             "id": 1,
     *             "name": "교대역"
     *         },
     *         "target": {
     *             "id": 3,
     *             "name": "양재역"
     *         }
     *     }
     * ]
     */
    @Test
    void show() {

    }

    /**
     * # 삭제 API
     * ## 요청
     * - DELETE /favorites/{id}
     * - authorization: Bearer
     * ---
     * ## 응답
     * - 204 No Content
     */
    @Test
    void delete() {

    }


}
