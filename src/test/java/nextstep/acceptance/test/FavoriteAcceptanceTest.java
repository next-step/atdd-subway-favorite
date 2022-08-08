package nextstep.acceptance.test;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.step.StationSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.acceptance.step.AuthSteps.로그인이_필요하다;
import static nextstep.acceptance.step.FavoriteSteps.*;
import static nextstep.acceptance.step.LineSteps.지하철_노선_생성_요청;
import static nextstep.acceptance.step.LineSteps.지하철_노선에_지하철_구간_생성_요청;
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

        교대역 = StationSteps.지하철역_생성("교대역").jsonPath().getLong("id");
        강남역 = StationSteps.지하철역_생성("강남역").jsonPath().getLong("id");
        남부터미널역 = StationSteps.지하철역_생성("남부터미널역").jsonPath().getLong("id");
        양재역 = StationSteps.지하철역_생성("양재역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("3호선", "orange").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(교대역, 강남역, 10));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 교대역, 3));
    }

    @DisplayName("로그인하면 즐겨찾기를 추가할 수 있다.")
    @Test
    void 즐겨찾기_생성() {
        // when
        var createResponse = 즐겨찾기_생성_요청(교대역, 강남역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        즐겨찾기_개수가_일치한다(1);
    }

    @DisplayName("로그인없이 즐겨찾기를 추가할 수 없다.")
    @Test
    void 즐겨찾기_생성_예외1() {
        // when
        var createResponse = 로그인_없이_즐겨찾기_생성_요청();

        // then
        로그인이_필요하다(createResponse);
    }

    @DisplayName("존재하지 않는 역을 즐겨찾기 추가할 수 없다.")
    @Test
    void 즐겨찾기_생성_예외2() {
        // given
        Long 존재하지_않는_역 = Long.MAX_VALUE;

        // when
        var createResponse = 즐겨찾기_생성_요청(강남역, 존재하지_않는_역);

        // then
        요청에_실패한다(createResponse);
    }

    @DisplayName("즐겨찾기를 중복으로 추가할 수 없다.")
    @Test
    void 즐겨찾기_생성_예외3() {
        // given
        var response = 즐겨찾기_생성_요청(교대역, 강남역);

        // when
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        var duplicateResponse = 즐겨찾기_생성_요청(교대역, 강남역);

        // then
        요청에_실패한다(duplicateResponse);
    }

    @DisplayName("로그인하면 즐겨찾기를 조회할 수 있다.")
    @Test
    void 즐겨찾기_조회() {
        // given
        즐겨찾기_생성_요청(교대역, 강남역);
        즐겨찾기_생성_요청(교대역, 양재역);

        // when + then
        List<Long> 즐겨찾기_출발역 = List.of(this.교대역, 교대역);
        List<Long> 즐겨찾기_종착역 = List.of(this.강남역, 양재역);
        즐겨찾기_조회_정보가_일치한다(즐겨찾기_출발역, 즐겨찾기_종착역);
    }

    @DisplayName("로그인하면 즐겨찾기를 삭제할 수 있다.")
    @Test
    void 즐겨찾기_삭제() {
        // given
        var createResponse = 즐겨찾기_생성_요청(교대역, 강남역);

        // when
        즐겨찾기_개수가_일치한다(1);
        String deleteLocation = createResponse.header("location");
        var deleteResponse = 즐겨찾기_삭제_요청(deleteLocation);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        즐겨찾기_개수가_일치한다(0);
    }

    @DisplayName("존재하지 않는 즐겨찾기를 삭제해도 아무일이 일어나지 않는다.")
    @Test
    void 즐겨찾기_삭제_예외2() {
        // given
        즐겨찾기_생성_요청(교대역, 강남역);

        // when
        즐겨찾기_개수가_일치한다(1);
        Long 존재하지_않는_즐겨찾기 = Long.MAX_VALUE;
        var invalidDeleteResponse = 즐겨찾기_삭제_요청(존재하지_않는_즐겨찾기);

        // then
        assertThat(invalidDeleteResponse.statusCode()).isIn(HttpStatus.NO_CONTENT.value());
        즐겨찾기_개수가_일치한다(1);
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

    private void 요청에_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isIn(HttpStatus.BAD_REQUEST.value());
    }

    private void 즐겨찾기_개수가_일치한다(int expected) {
        var response = 즐겨찾기_조회_요청();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".")).hasSize(expected);
    }

    private void 즐겨찾기_조회_정보가_일치한다(List<Long> sourceIds, List<Long> targetIds) {
        var response = 즐겨찾기_조회_요청();

        assertThat(response.jsonPath().getList("source.id", Long.class))
                .containsExactlyElementsOf(sourceIds);
        assertThat(response.jsonPath().getList("target.id", Long.class))
                .containsExactlyElementsOf(targetIds);
    }
}
