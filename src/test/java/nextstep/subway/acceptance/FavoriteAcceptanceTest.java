package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 신분당선;
    private Long 이호선;
    private Long 강남역;
    private Long 양재역;
    private Long 교대역;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청(ADMIN_ACCESS_TOKEN, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(ADMIN_ACCESS_TOKEN, "양재역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청(ADMIN_ACCESS_TOKEN, "교대역").jsonPath().getLong("id");

        신분당선 = 지하철_노선_생성_요청(ADMIN_ACCESS_TOKEN, createLineCreateParams("신분당선", "red", 강남역, 양재역)).jsonPath().getLong("id");
        이호선 = 지하철_노선_생성_요청(ADMIN_ACCESS_TOKEN, createLineCreateParams("2호선", "green", 교대역, 강남역)).jsonPath().getLong("id");
    }

    /**
     * feature : 즐겨찾기 생성
     * Given 강남역 생성
     * And 양재역 생성
     * And 교대역 생성
     * And 신분당선 노선 생성 [강남역 - 양재역]
     * And 2호선 노선 생성 [교대역 - 강남역]
     * And 로그인이 되어 있음
     */
    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void createStation() {

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(ADMIN_ACCESS_TOKEN, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        // given
        즐겨찾기_생성_요청(ADMIN_ACCESS_TOKEN, 강남역, 양재역);
        즐겨찾기_생성_요청(ADMIN_ACCESS_TOKEN, 교대역, 강남역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        // then
        List<String> sourceStationNames = response.jsonPath().getList("source.name", String.class);
        List<String> targetStationNames = response.jsonPath().getList("target.name", String.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(sourceStationNames).containsExactly("강남역", "교대역"),
                () -> assertThat(targetStationNames).containsExactly("양재역", "강남역")
        );
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(ADMIN_ACCESS_TOKEN, 교대역, 강남역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(ADMIN_ACCESS_TOKEN, createResponse.header("location"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, String> createLineCreateParams(String name, String color, Long upStationId, Long downStationId) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", 10 + "");
        return lineCreateParams;
    }

}