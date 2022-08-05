package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
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
        final ExtractableResponse<Response> response = 즐겨찾기_등록();
        final long sourceId = response.jsonPath().getLong("source.id");
        final long targetId = response.jsonPath().getLong("target.id");

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(sourceId).isEqualTo(강남역),
            () -> assertThat(targetId).isEqualTo(양재역)
        );
    }

    @Test
    @DisplayName("즐겨찾기 조회")
    void getFavorite() {
        즐겨찾기_등록();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회() {
        return AuthCommon.given(관리자_토큰)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_등록() {
        return AuthCommon.given(관리자_토큰)
            .body(favoriteParams())
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
