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
     * Given Token 인증을 통해 로그인한다.
     * When 경로(출발역, 도착역)을 즐겨찾기로 추가하면
     * Then 즐겨찾기 추가 성공을 응답받는다.
     */
    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void createStation() {

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(ADMIN_ACCESS_TOKEN, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    /**
     * Given Token 인증을 통해 로그인한다.
     * Given 2개의 경로(출발역, 도착역)을 즐겨찾기로 추가하고
     * When 나의 즐겨찾기 목록을 조회하면
     * Then 즐겨찾기 목록에 2개의 경로를 확인할 수 있다.
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        // given
        즐겨찾기_생성_요청(ADMIN_ACCESS_TOKEN, 강남역, 양재역);
        즐겨찾기_생성_요청(ADMIN_ACCESS_TOKEN, 교대역, 강남역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(ADMIN_ACCESS_TOKEN);

        // then
        List<String> sourceStationNames = response.jsonPath().getList("source.name", String.class);
        List<String> targetStationNames = response.jsonPath().getList("target.name", String.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(sourceStationNames).containsExactly("강남역", "교대역"),
                () -> assertThat(targetStationNames).containsExactly("양재역", "강남역")
        );
    }

    /**
     * Given Token 인증을 통해 로그인한다.
     * Given 경로(출발역, 도착역)을 즐겨찾기로 추가하고
     * When 즐겨찾기를 삭제하면
     * Then 해당 즐겨찾기 정보는 삭제된다.
     * Then 즐겨찾기 목록 조회시 조회되지 않는다.
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(ADMIN_ACCESS_TOKEN, 교대역, 강남역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(ADMIN_ACCESS_TOKEN, createResponse.header("location"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(즐겨찾기_목록_조회_요청(ADMIN_ACCESS_TOKEN).jsonPath().getList("")).isEmpty();
    }

    /**
     * Given 로그인하지 않고
     * When 경로(출발역, 도착역)을 즐겨찾기로 추가하면
     * Then 401 Unauthorized Exception 을 만난다.
     */
    @DisplayName("비로그인 회원이 즐겨찾기 경로를 추가하면 예외")
    @Test
    void anonymousUserAddFavoritePathException() {

        // when
        ExtractableResponse<Response> response = 비로그인_즐겨찾기_생성_요청(강남역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
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