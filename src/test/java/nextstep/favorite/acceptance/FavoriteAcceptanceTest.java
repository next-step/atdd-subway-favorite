package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.SectionStep;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.ErrorTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.auth.acceptance.step.TokenSteps.일반_로그인_요청;
import static nextstep.auth.acceptance.step.TokenSteps.토큰_추출;
import static nextstep.favorite.acceptance.step.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.member.acceptance.step.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.step.LineStep.지하철_노선을_생성한다;
import static nextstep.subway.acceptance.step.StationStep.지하철역을_생성한다;
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

        교대역 = Id_추출(지하철역을_생성한다("교대역"));
        강남역 = Id_추출(지하철역을_생성한다("강남역"));
        양재역 = Id_추출(지하철역을_생성한다("양재역"));
        남부터미널역 = Id_추출(지하철역을_생성한다("남부터미널역"));

        이호선 = Id_추출(지하철_노선을_생성한다("2호선", "green", 교대역, 강남역, 10));
        신분당선 = Id_추출(지하철_노선을_생성한다("신분당선", "red", 강남역, 양재역, 10));
        삼호선 = Id_추출(지하철_노선을_생성한다("3호선", "orange", 교대역, 남부터미널역, 2));

        SectionStep.지하철_노선_구간을_등록한다(삼호선, 남부터미널역, 양재역, 3);
    }

    private Long Id_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    /*
     * 공통
     * - 권한 없음 -> 401 Unauthorized
     */

    /**
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
     * ## 시나리오
     * Given : 지하철역과 노선을 생성하고 (@BeforeEach)
     * And : 가짜 토큰을 사용하여
     * When : 출발역 id와 도착역 id를 전송하면
     * Then : 401 UnAuthorized가 발생한다.
     */
    @DisplayName("즐겨찾기 생성 실패 : 올바른 토큰이 아닌 경우")
    @Test
    void createFailByFakeToken() {
        // given
        String fakeToken = "가짜 토큰";

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(fakeToken, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        ErrorTestUtils.예외_메세지_검증(response, ErrorCode.INVALID_TOKEN_EXCEPTION);
    }

    /**
     * Given : 지하철역과 노선을 생성하고 (@BeforeEach)
     * And : 회원을 생성하고
     * And : 토큰을 발급받은 후
     * When : 서로 같은 출발역 id와 도착역 id를 전송하면
     * Then : 400 BadRequest가 발생한다.
     */
    @DisplayName("즐겨찾기 생성 실패 : 역 id가 서로 같은 경우")
    @Test
    void createFailBySameId() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 토큰_추출(일반_로그인_요청(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역, 교대역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorTestUtils.예외_메세지_검증(response, ErrorCode.SAME_SOURCE_AND_TARGET_STATION);
    }

    /**
     * Given : 지하철역과 노선을 생성하고 (@BeforeEach)
     * And : 회원을 생성하고
     * And : 토큰을 발급받은 후
     * When : 저장되지 않은 출발역 id와 도착역 id를 전송하면
     * Then : 404 NotFound가 발생한다.
     */
    @DisplayName("즐겨찾기 생성 실패 : 역 id에 해당하는 Station이 없는 경우")
    @Test
    void createFailByStationNotFound() {
        // given
        Long 없는역 = -1L;

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 토큰_추출(일반_로그인_요청(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역, 없는역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ErrorTestUtils.예외_메세지_검증(response, ErrorCode.STATION_NOT_FOUND);
    }

    /**
     * Given : 지하철역과 노선을 생성하고 (@BeforeEach)
     * And : 경로가 연결되지 않은 지하철 노선을 생성하고
     * And : 회원을 생성하고
     * And : 토큰을 발급받은 후
     * When : 경로가 없는 출발역 id 또는 도착역 id를 전송하면
     * Then : 404 NotFound가 발생한다.
     */
    @DisplayName("즐겨찾기 생성 실패 : 경로가 없는 경우")
    @Test
    void createFailByStationPathNotFound() {
        // given
        Long 증미역 = Id_추출(지하철역을_생성한다("증미역"));
        Long 등촌역 = Id_추출(지하철역을_생성한다("등촌역"));
        Id_추출(지하철_노선을_생성한다("9호선", "brown", 증미역, 등촌역, 10));

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 토큰_추출(일반_로그인_요청(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역, 등촌역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ErrorTestUtils.예외_메세지_검증(response, ErrorCode.PATH_NOT_FOUND);
    }

    /**
     * Given : 지하철역과 노선을 생성하고 (@BeforeEach)
     * And : 주어진 노선 상에 없는 역을 생성하고
     * And : 회원을 생성하고
     * And : 토큰을 발급받은 후
     * When : 주어진 노선 상에 없는 출발역 id 또는 도착역 id를 전송하면
     * Then : 400 BadRequest가 발생한다.
     */
    @DisplayName("즐겨찾기 생성 실패 : 역이 주어진 노선 상에 없는 역인 경우")
    @Test
    void createFailByStationNotInGivenLine() {
        // given
        Long 노선에_없는역 = Id_추출(지하철역을_생성한다("노선에 없는역"));

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 토큰_추출(일반_로그인_요청(EMAIL, PASSWORD));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역, 노선에_없는역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorTestUtils.예외_메세지_검증(response, ErrorCode.STATION_NOT_IN_GIVEN_LINES);
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
