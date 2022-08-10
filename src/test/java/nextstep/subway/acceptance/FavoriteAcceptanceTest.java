package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

class FavoriteAcceptanceTest extends AcceptanceTest {

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
        교대역 = 지하철역_생성_요청(관리자_토큰, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(관리자_토큰, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자_토큰, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(관리자_토큰, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청(관리자_토큰, "2호선", "green", 교대역, 강남역, 10).jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청(관리자_토큰, "신분당선", "red", 강남역, 양재역, 10).jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청(관리자_토큰, "3호선", "orange", 교대역, 남부터미널역, 2).jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(관리자_토큰, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
    }

    @DisplayName("즐겨찾기에 추가한다.")
    @Test
    void createFavorite() {
        즐겨찾기_생성_요청(관리자_토큰, 교대역, 양재역);

        List<Long> sources = 즐겨찾기_조회_요청(관리자_토큰).jsonPath().getList("source.id", Long.class);
        List<Long> targets = 즐겨찾기_조회_요청(관리자_토큰).jsonPath().getList("target.id", Long.class);
        assertAll(
                () -> assertThat(sources).containsExactly(교대역),
                () -> assertThat(targets).containsExactly(양재역)
        );
    }

    @DisplayName("로그인 되어있지 않으면 즐겨찾기를 생성할 수 없다.")
    @Test
    void createFavoriteWithoutToken() {
        assertThat(토큰없이_즐겨찾기_생성_요청(교대역, 양재역).statusCode())
                .isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("이미 존재하는 즐겨찾기는 추가할 수 없다.")
    @Test
    void createExistFavorite() {
        즐겨찾기_생성_요청(관리자_토큰, 교대역, 양재역);

        assertThat(즐겨찾기_생성_요청(관리자_토큰, 교대역, 양재역).statusCode())
                .isEqualTo(BAD_REQUEST.value());
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void findFavorite() {
        즐겨찾기_생성_요청(관리자_토큰, 교대역, 양재역);
        즐겨찾기_생성_요청(관리자_토큰, 남부터미널역, 양재역);
        즐겨찾기_생성_요청(관리자_토큰, 강남역, 교대역);

        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(관리자_토큰);
        List<Long> sources = response.jsonPath().getList("source.id", Long.class);
        List<Long> targets = response.jsonPath().getList("target.id", Long.class);
        assertAll(
                () -> assertThat(sources).containsExactly(교대역, 남부터미널역, 강남역),
                () -> assertThat(targets).containsExactly(양재역, 양재역, 교대역)
        );
    }

    @DisplayName("로그인 되어있지 않으면 즐겨찾기를 조회할 수 없다.")
    @Test
    void findFavoriteWithoutToken() {
        즐겨찾기_생성_요청(관리자_토큰, 교대역, 양재역);

        assertThat(토큰없이_즐겨찾기_조회_요청().statusCode())
                .isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        ExtractableResponse<Response> 즐겨찾기_응답 = 즐겨찾기_생성_요청(관리자_토큰, 교대역, 양재역);

        즐겨찾기_제거_요청(관리자_토큰, 즐겨찾기_응답.header("location"));

        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(관리자_토큰);
        List<Long> sources = response.jsonPath().getList("source.id", Long.class);
        List<Long> targets = response.jsonPath().getList("target.id", Long.class);
        assertAll(
                () -> assertThat(sources).isEmpty(),
                () -> assertThat(targets).isEmpty()
        );
    }

    @DisplayName("로그인 되어있지 않으면 즐겨찾기를 삭제할 수 없다.")
    @Test
    void deleteFavoriteWithoutToken() {
        ExtractableResponse<Response> 즐겨찾기 = 즐겨찾기_생성_요청(관리자_토큰, 교대역, 양재역);

        assertThat(토큰없이_즐겨찾기_제거_요청(즐겨찾기.header("location")).statusCode())
                .isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("존재하지 않는 즐겨찾기는 삭제할 수 없다.")
    @Test
    void deleteNonExistentFavorite() {
        assertThat(즐겨찾기_제거_요청(관리자_토큰, "/favorites/100").statusCode())
                .isEqualTo(BAD_REQUEST.value());
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}
