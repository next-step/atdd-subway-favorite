package nextstep.subway.acceptance.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.step.LineSteps.*;
import static nextstep.subway.acceptance.step.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.step.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.step.StationSteps.지하철역_생성_요청;
import static nextstep.subway.utils.RestAssuredStep.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        //when
        Map<String, Long> params = createFavoriteRequestParam(구일역, 신도림역);
        ExtractableResponse<Response> response =  즐겨찾기_등록_요청(관리자토큰, params);
        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.jsonPath().getString("target.name")).contains("신도림역"),
                () -> assertThat(response.jsonPath().getString("source.name")).contains("구일역")
        );
    }

    /**
     * when 존재하지 않는 지하철역에 대한 즐겨찾기 등록을 요청한다.
     * when 즐겨찾기 경로 등록이 실패한다.
     */
    @DisplayName("존재하지 않는 지하철역 요청으로 인한 즐겨찾기 등록 실패.")
    @Test
    void addFavoriteFail() {
        Map<String, Long> params = createFavoriteRequestParam(9L, 10L);
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(관리자토큰, params);

        assertAllHttpStatusAndMessage(response, HttpStatus.NOT_FOUND.value(), "등록된 지하철역으로 요청하셔야 합니다.");
    }

    /**
     * when 로그인한 사용자가 즐겨찾기 목록을 조회하면
     * then 등록된 즐겨찾기 목록이 조회된다.
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void getFavoriteListAfterLogin() {

        Map<String, Long> params = createFavoriteRequestParam(구일역, 신도림역);
        즐겨찾기_등록_요청(관리자토큰, params);
        Map<String, Long> params2 = createFavoriteRequestParam(오류역, 개봉역);
        즐겨찾기_등록_요청(관리자토큰, params2);

        given(관리자토큰)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    /**
     * given 즐겨찾기를 등록한다.
     * when 로그인한 사용자가 즐겨찾기 목록을 삭제하면
     * then 해당 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기 목록을 삭제한다.")
    @Test
    void deleteFavoriteAfterLogin() {
        Map<String, Long> params = createFavoriteRequestParam(구일역, 신도림역);
        Long favoriteId = 즐겨찾기_등록_요청(관리자토큰, params).jsonPath().getLong("id");

        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(관리자토큰, favoriteId);

        검증_NO_CONTENT(response);
    }

    /**
     * when 본인의 것이 아닌 즐겨찾기 제거요청
     * then 해당 즐겨찾기가 삭제가 실패한다.
     */
    @DisplayName("본인의 것이 아닌 즐겨찾기 삭제요청시 실패한다.")
    @Test
    void deleteFavoritefail() {
        Map<String, Long> params = createFavoriteRequestParam(구일역, 신도림역);
        Long favoriteId = 즐겨찾기_등록_요청(관리자토큰, params).jsonPath().getLong("id");

        검증_UNAUTHORIZED(즐겨찾기_삭제_요청(일반사용자토큰, favoriteId));
    }

    private ExtractableResponse<Response> 즐겨찾기_등록_요청(String 토큰, Map<String, Long> params){
        return given(토큰)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String 토큰, Long favoriteId){
        return given(토큰)
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    private Map<String, Long> createFavoriteRequestParam(Long sourceId, Long targetId){
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);
        return params;
    }

    private void assertAllHttpStatusAndMessage(ExtractableResponse<Response> response, int httpStatus, String message){
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(httpStatus),
                () -> assertThat(response.jsonPath().getString("message")).contains(message)
        );
    }

}