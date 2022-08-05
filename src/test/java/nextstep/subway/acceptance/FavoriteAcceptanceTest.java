package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 남부터미널역;
    private Long 양재역;
    private Long 이호선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선(10)* ---   강남역
     *  |
     * 남부터미널역  --- *3호선(3)* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성("강남역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성("남부터미널역").jsonPath().getLong("id");
        양재역 = 지하철역_생성("양재역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("3호선", "orange").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(교대역, 강남역, 10));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 교대역, 3));
    }

    @DisplayName("로그인하면 즐겨찾기를 추가할 수 있다.")
    @Test
    void 즐겨찾기() {
        // when
        var response = 즐겨찾기_생성_요청(교대역, 강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("로그인없이 즐겨찾기 할 수 없다.")
    @Test
    void 즐겨찾기_예외1() {
        // when
        var response = 로그인_없이_즐겨찾기_생성_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("존재하지 않는 역을 즐겨찾기 할 수 없다.")
    @Test
    void 즐겨찾기_예외2() {
        // given
        Long 존재하지_않는_역 = Long.MAX_VALUE;

        // when
        var response = 즐겨찾기_생성_요청(강남역, 존재하지_않는_역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("즐겨찾기를 중복으로 할 수 없다.")
    @Test
    void 즐겨찾기_예외3() {
        // given
        var response = 즐겨찾기_생성_요청(교대역, 강남역);

        // when
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        var duplicateResponse = 즐겨찾기_생성_요청(교대역, 강남역);

        // then
        assertThat(duplicateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("로그인하면 즐겨찾기를 조회할 수 있다.")
    @Test
    void 즐겨찾기_조회() {
        // given
        Long 즐겨찾기1번 = 즐겨찾기_생성_요청(교대역, 강남역).jsonPath().getLong("id");
        Long 즐겨찾기2번 = 즐겨찾기_생성_요청(교대역, 양재역).jsonPath().getLong("id");

        // when
        var response = 즐겨찾기_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id", Long.class))
                .containsExactlyInAnyOrder(즐겨찾기1번, 즐겨찾기2번);
    }

    @DisplayName("로그인하면 즐겨찾기를 삭제할 수 있다.")
    @Test
    void 즐겨찾기_삭제() {
        // given
        Long 즐겨찾기1번 = 즐겨찾기_생성_요청(교대역, 강남역).jsonPath().getLong("id");
        Long 즐겨찾기2번 = 즐겨찾기_생성_요청(교대역, 양재역).jsonPath().getLong("id");

        // when
        var deleteResponse = 즐겨찾기_삭제_요청(즐겨찾기1번);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        var response = 즐겨찾기_조회_요청();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id", Long.class))
                .containsExactlyInAnyOrder(즐겨찾기2번);
    }

    @DisplayName("다른 유저의 즐겨찾기를 삭제할 수 없다.")
    @Test
    void 즐겨찾기_삭제_예외() {
        // given
        Long 다른_유저_즐겨찾기 = 즐겨찾기_생성_요청_관리자(교대역, 강남역).jsonPath().getLong("id");

        // when
        var deleteResponse = 즐겨찾기_삭제_요청(다른_유저_즐겨찾기);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("존재하지 않는 즐겨찾기를 삭제할 수 없다.")
    @Test
    void 즐겨찾기_삭제_예외2() {
        // given
        Long 존재하지_않는_즐겨찾기 = Long.MAX_VALUE;

        // when
        var deleteResponse = 즐겨찾기_삭제_요청(존재하지_않는_즐겨찾기);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 로그인_없이_즐겨찾기_생성_요청() {
        return RestAssured.given().log().all()
                .body(createFavoritesCreateParams(교대역, 강남역))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
