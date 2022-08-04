package nextstep.subway.acceptance.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.step.LineSteps.*;
import static nextstep.subway.acceptance.step.StationSteps.지하철역_생성_요청;
import static nextstep.subway.utils.RestAssuredStep.given;
import static org.assertj.core.api.Assertions.assertThat;

class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 신분당선;

    private Long 오류역;
    private Long 개봉역;
    private Long 구일역;
    private Long 구로역;
    private Long 신도림역;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        오류역 = 지하철역_생성_요청(관리자토큰,"오류역").jsonPath().getLong("id");
        개봉역 = 지하철역_생성_요청(관리자토큰,"개봉역").jsonPath().getLong("id");

        Map<String, String> lineParams = createLineCreateParams(오류역, 개봉역);
        신분당선 = 지하철_노선_생성_요청(관리자토큰, lineParams).jsonPath().getLong("id");

        구일역 = 지하철역_생성_요청(관리자토큰,"구일역").jsonPath().getLong("id");
        구로역 = 지하철역_생성_요청(관리자토큰,"구로역").jsonPath().getLong("id");
        신도림역 = 지하철역_생성_요청(관리자토큰,"신도림역").jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(관리자토큰, 신분당선, createSectionCreateParams(개봉역, 구일역));
        지하철_노선에_지하철_구간_생성_요청(관리자토큰, 신분당선, createSectionCreateParams(구일역, 구로역));
        지하철_노선에_지하철_구간_생성_요청(관리자토큰, 신분당선, createSectionCreateParams(구로역, 신도림역));
    }

    /**
     * when 로그인 후 사용자가 즐겨찾기를 등록한다.
     * when 즐겨찾기 경로가 등록된다.
     */
    @DisplayName("즐겨찾기를 등록한다.")
    @Test
    void addFavoriteAfterLogin() {
        Map<String, Long> params = new HashMap<>();
        params.put("source", 구일역);
        params.put("target", 신도림역);
        즐겨찾기_등록_요청(params);

    }

    /**
     * given 즐겨찾기를 등록한다.
     * when 로그인한 사용자가 즐겨찾기 목록을 조회하면
     * then 등록된 즐겨찾기 목록이 조회된다.
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    void getFavoriteListAfterLogin() {
    }

    /**
     * given 즐겨찾기를 등록한다.
     * when 로그인한 사용자가 즐겨찾기 목록을 삭제하면
     * then 해당 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기 목록을 삭제한다..")
    void deleteFavoriteAfterLogin() {
    }

    private ExtractableResponse<Response> 즐겨찾기_등록_요청(Map<String, Long> params){
        return given(관리자토큰)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

}