package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class FavoriteAcceptanceTest extends AcceptanceTest {

    long 강남역;
    long 양재역;
    long 교대역;
    long 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationSteps.지하철역_생성_요청(관리자_토큰, "강남역").jsonPath().getLong("id");
        양재역 = StationSteps.지하철역_생성_요청(관리자_토큰, "양재역").jsonPath().getLong("id");
        교대역 = StationSteps.지하철역_생성_요청(관리자_토큰, "교대역").jsonPath().getLong("id");
        이호선 = LineSteps.지하철_노선_생성_요청(관리자_토큰, "2호선", "green").jsonPath().getLong("id");

        LineSteps.지하철_노선에_지하철_구간_생성_요청(관리자_토큰, 이호선, createSectionCreateParams(강남역, 양재역));
        LineSteps.지하철_노선에_지하철_구간_생성_요청(관리자_토큰, 이호선, createSectionCreateParams(양재역, 교대역));
    }

    @Test
    @DisplayName("즐겨찾기 등록")
    void createFavorite() {
        final ExtractableResponse<Response> response = 즐겨찾기_등록(favoriteParams());
        final long sourceId = response.jsonPath().getLong("source.id");
        final long targetId = response.jsonPath().getLong("target.id");

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(sourceId).isEqualTo(강남역),
            () -> assertThat(targetId).isEqualTo(양재역)
        );
    }

    @Test
    @DisplayName("즐겨찾기 등록 시 없는 역 등록할 경우 예외")
    void createFavoriteException() {
        final ExtractableResponse<Response> response = 즐겨찾기_등록(Map.of(
            "source", Long.MAX_VALUE,
            "target", Long.MAX_VALUE
        ));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("즐겨찾기 등록 시 권한이 없는 경우")
    void notAuthenticate() {
        final ExtractableResponse<Response> response = RestAssured.given()
            .body(Map.of(
                "source", 1L,
                "target", 2L
            ))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    void getFavorite() {
        //given
        즐겨찾기_등록(favoriteParams());

        //when
        final ExtractableResponse<Response> response = 즐겨찾기_조회();

        //then
        final List<String> sources = response.jsonPath().getList("source.name", String.class);
        final List<String> targets = response.jsonPath().getList("target.name", String.class);
        assertThat(sources).hasSize(1);
        assertThat(targets).hasSize(1);
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void removeFavorite() {
        //given
        final long 즐겨찾기ID = 즐겨찾기_등록(favoriteParams()).jsonPath().getLong("id");

        //when
        final ExtractableResponse<Response> response = 즐겨찾기_삭제(즐겨찾기ID);

        final ExtractableResponse<Response> 즐겨찾기_조회 = 즐겨찾기_조회();
        final List<Object> root = 즐겨찾기_조회.jsonPath().getList("");

        //then
        assertThat(root).isEmpty();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제(long id) {
        return AuthCommon.given(관리자_토큰)
            .when().delete("/favorites/" + id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회() {
        return AuthCommon.given(관리자_토큰)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_등록(Map<String, Long> params) {
        return AuthCommon.given(관리자_토큰)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    private Map<String, Long> favoriteParams() {
        return Map.of(
            "source", 강남역,
            "target", 양재역
        );
    }


    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", 6 + "");
        return params;
    }

}
