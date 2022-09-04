package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.DataLoader.*;
import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private String 일반_사용자;
    private String 관리자;
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
        일반_사용자 = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);
        관리자 = 로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD);

        교대역 = 지하철역_생성_요청(관리자, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);
    }

    /**
     * When 지하철 즐겨찾기 노선을 생성하면,
     * Then 즐겨찾기 노선이 생성된다.
     */
    @DisplayName("지하철 즐겨찾기 노선 생성")
    @Test
    void 즐겨찾기_노선을_생성한다() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_즐겨찾기_생성(일반_사용자, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * When 지하철 즐겨찾기 노선을 생성하고,
     * Then 즐겨찾기 노선을 조회하면,
     * Then 생성한 즐겨찾기 노선이 조회된다.
     */
    @DisplayName("지하철 즐겨찾기 노선 조회")
    @Test
    void 즐겨찾기_노선이_조회된다() {
        // when
        지하철_노선_즐겨찾기_생성(일반_사용자, 교대역, 양재역);

        // then
        ExtractableResponse<Response> response = 지하철_노선_즐겨찾기_조회_요청(일반_사용자);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> Assertions.assertThat(response.jsonPath().getList("source.name", String.class)).containsExactly("교대역"),
                () -> Assertions.assertThat(response.jsonPath().getList("target.name", String.class)).containsExactly("양재역")
        );
    }

    /**
     * When 지하철 즐겨찾기 노선을 2번 생성하고,
     * Then 즐겨찾기 노선을 조회하면,
     * Then 2번 생성한 즐겨찾기 노선이 전부 조회된다.
     */
    @DisplayName("여러 지하철 즐겨찾기 노선 조회")
    @Test
    void 즐겨찾기_노선_모두가_조회된다() {
        // when
        지하철_노선_즐겨찾기_생성(일반_사용자, 교대역, 양재역);
        지하철_노선_즐겨찾기_생성(일반_사용자, 남부터미널역, 양재역);

        // then
        ExtractableResponse<Response> response = 지하철_노선_즐겨찾기_조회_요청(일반_사용자);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> Assertions.assertThat(response.jsonPath().getList("source.name", String.class)).containsExactly("교대역", "남부터미널역"),
                () -> Assertions.assertThat(response.jsonPath().getList("target.name", String.class)).containsExactly("양재역", "양재역")
        );
    }

    /**
     * When 지하철 즐겨찾기 노선을 생성하고,
     * Then 즐겨찾기 노선을 삭제하면,
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기 노선 삭제")
    @Test
    void 즐겨찾기_노선이_삭제된다() {
        // when
        String 즐겨찾기_노선 = 지하철_노선_즐겨찾기_생성(일반_사용자, 교대역, 양재역).header("location");

        // then
        ExtractableResponse<Response> response = 지하철_노선_즐겨찾기_제거_요청(일반_사용자, 즐겨찾기_노선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 즐겨찾기 노선을 생성하고,
     * When 유효하지 않은 토큰이 즐겨찾기를 삭제 요청하면,
     * Then 권한이 없음(401) 을 응답한다.
     */
    @DisplayName("유효하지 않은 로그인 정보로 삭제할 수 없다")
    @Test
    void 유효하지_않은_토큰으로_로그인_삭제_요청() {
        // given
        String 즐겨찾기_노선 = 지하철_노선_즐겨찾기_생성(일반_사용자, 교대역, 양재역).header("location");

        // when
        ExtractableResponse<Response> response = 지하철_노선_즐겨찾기_제거_요청("invalid", 즐겨찾기_노선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 지하철 즐겨찾기 노선을 생성하고,
     * When 비로그인으로 즐겨찾기를 삭제 요청하면,
     * Then 권한이 없음(401) 을 응답한다.
     */
    @DisplayName("로그인되어 있지 않으면 삭제할 수 없다")
    @Test
    void 로그인하지_않은_즐겨찾기_삭제_요청() {

        // given
        String 즐겨찾기_노선 = 지하철_노선_즐겨찾기_생성(일반_사용자, 교대역, 양재역).header("location");

        // when
        ExtractableResponse<Response> response = 지하철_노선_즐겨찾기_제거_요청(즐겨찾기_노선);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Givn 지하철 즐겨찾기 노선을 생성하고,
     * When 다시 등록되어 있는 지하철 즐겨찾기 노선을 생성하면,
     * Then 올바르지 않은 요청이기 떄문에 잘못된 요청(403)을 응답한다.
     */
    @DisplayName("")
    @Test
    void 중복된_즐겨찾기_노선_생성_요청() {

        // given
        지하철_노선_즐겨찾기_생성(일반_사용자, 교대역, 양재역).header("location");

        // when
        ExtractableResponse<Response> response = 지하철_노선_즐겨찾기_생성(일반_사용자, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");

        return LineSteps.지하철_노선_생성_요청(관리자, lineCreateParams).jsonPath().getLong("id");
    }
}
