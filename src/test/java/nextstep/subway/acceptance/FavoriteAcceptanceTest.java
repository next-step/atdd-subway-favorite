package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.createLineCreateParams;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 생성 요청 시 등록이 된다")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private long 강남역;
    private long 신주쿠역;
    private long 교대역;
    private long 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        신주쿠역 = 지하철역_생성_요청("신주쿠역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");


        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 교대역);
        이호선 = 지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    /**
     * When 즐겨찾기를 생성 요청 시
     * Then 등록이 된다
     */
    @DisplayName("즐겨찾기 생성 요청 시 등록이 된다")
    @Test
    void 즐겨찾기_생성_요청_시_등록이_된다() {
        // When
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(강남역, 교대역, 사용자_AccessToken);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/favorites/1");
    }

    /**
     * When 연결되어 있지 않은 경로 즐겨찾기 생성 요청 시
     * Then 등록이 안된다
     */
    @DisplayName("연결되어 있지 않은 경로로 즐겨찾기 생성 요청 시 등록이 안된다")
    @Test
    void 연결되어_있지_않은_경로_즐겨찾기_생성_요청_시_등록이_안된다() {
        // When
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(강남역, 신주쿠역, 사용자_AccessToken);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 즐겨찾기 등록 요청 후
     * When 즐겨찾기 조회 요청 시
     * Then 조회가 된다
     */
    @DisplayName("즐겨찾기 조회 요청 시 조회가 된다")
    @Test
    void 즐겨찾기_조회_요청_시_조회가_된다() {
        // Given
        즐겨찾기_등록_요청(강남역, 교대역, 사용자_AccessToken);

        // When
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(사용자_AccessToken);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<FavoriteResponse> responses = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(responses).containsExactly(new FavoriteResponse(1L, new StationResponse(1L, "강남역"), new StationResponse(3L, "교대역")));
    }

    /**
     * Given 즐겨찾기 등록 요청 후
     * When 즐겨찾기 조회 요청 시
     * Then 조회가 된다
     */
    @DisplayName("즐겨찾기 삭제 요청 시 삭제가 된다")
    @Test
    void 즐겨찾기_조회_삭제_요청_시_삭제가_된다() {
        // Given
        String locationId = 즐겨찾기_등록_요청(강남역, 교대역, 사용자_AccessToken)
                .header("Location")
                .replace("/favorites/", "");

        // When
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(locationId, 사용자_AccessToken);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * When 로그인 안한 사용자가 삭제 요청 시
     * Then 삭제 할 수 없다
     */
    @DisplayName("로그인 안한 사용자가 삭제 요청 시 삭제 할 수 없다")
    @Test
    void 로그인_안한_사용자가_삭제_요청_시_삭제_할_수_없다() {
        // When
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청("1", "WrongAccessToken");

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * When 로그인 안한 사용자가 조회 요청 시
     * Then 조회 할 수 없다
     */
    @DisplayName("로그인 안한 사용자가 조회 요청 시 조회 할 수 없다")
    @Test
    void 로그인_안한_사용자가_조회_요청_시_조회_할_수_없다() {
        // When
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청("WrongAccessToken");

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * When 로그인 안한 사용자가 등록 요청 시
     * Then 등록 할 수 없다
     */
    @DisplayName("로그인 안한 사용자가 등록 요청 시 등록 할 수 없다")
    @Test
    void 로그인_안한_사용자가_등록_요청_시_등록_할_수_없다() {
        // When
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(강남역, 교대역, "wrongToken");

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_등록_요청(Long source, Long target, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String favoriteId, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all().extract();
    }

}
