package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
    }

    /**
     * when 로그인한 사용자가 출발역과 도착역으로 구성된 즐겨찾기 경로를 등록하면
     * then 등록된 사용자의 즐겨찾기를 조회할수 있다
     */
    @DisplayName("즐겨찾기 등록")
    @Test
    void createFavorite() {
        // when
        var createResponse = 즐겨찾기_등록(교대역, 양재역);

        // then
        var favoriteResponse = 즐겨찾기_조회();
        var favorites = favoriteResponse.jsonPath().getList(".", FavoriteResponse.class);
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(favorites).hasSize(1),
                () -> assertThat(favorites.get(0).getSource().getId()).isEqualTo(교대역),
                () -> assertThat(favorites.get(0).getTarget().getId()).isEqualTo(양재역)
        );
    }

    /**
     * when 동일한 출발역, 도착역에 대해 즐겨찾기를 등록하면
     * then 즐겨찾기 등록이 실패한다
     */
    @DisplayName("출발역과 도착역이 동일한 즐겨찾기 등록 실패")
    @Test
    void createFavoriteFailsForSameStations() {
        // when
        var createResponse = 즐겨찾기_등록(교대역, 교대역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * when 존재하지 않는 역에 대해 즐겨찾기를 등록하면
     * then 즐겨찾기 등록이 실패한다
     */
    @DisplayName("존재하지 않는 역에 대한 즐겨찾기 등록 실패")
    @Test
    void createFavoriteFailsForStationNotExist() {
        // when
        var 존재하지_않는_역 = 123123L;
        var createResponse = 즐겨찾기_등록(교대역, 존재하지_않는_역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * given 사용자의 즐겨찾기를 등록하고
     * when 등록된 즐겨찾기를 삭제하면
     * then 삭제된 즐겨찾기가 조회되지 않는다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void removeFavorite() {
        // given
        var favoriteId = 즐겨찾기_등록(교대역, 양재역)
                .header("Location")
                .split("/favorites/")[1];

        // when
        var deleteResponse = 즐겨찾기_삭제(Long.valueOf(favoriteId));

        // then
        var favoriteResponse = 즐겨찾기_조회();
        var favorites = favoriteResponse.jsonPath().getList(".", FavoriteResponse.class);
        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(favorites).isEmpty()
        );
    }

    /**
     * given 사용자의 즐겨찾기를 등록하고
     * when 다른 사용자의 ID로 즐겨찾기를 삭제하면
     * then 에러가 발생한다
     */
    @DisplayName("보유하지 않은 즐겨찾기 삭제 실패")
    @Test
    void removeFavoriteFailsWhenUserNotMatch() {
        // given
        var favoriteId = 즐겨찾기_등록(교대역, 양재역)
                .header("Location")
                .split("/favorites/")[1];

        // when
        var deleteResponse = MemberSteps
                .givenLogin("user@email.com", "password")
                    .pathParam("id", favoriteId)
                .when()
                    .delete("/favorites/{id}")
                .then()
                    .extract();;

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * when 등록되지 않은 ID로 즐겨찾기를 삭제하면
     * then 에러가 발생한다
     */
    @DisplayName("존재하지 않는 즐겨찾기 삭제 실패")
    @Test
    void removeFavoriteFailsForIdNotExist() {
        // when
        var favoriteIdNotExist = 123123L;
        var deleteResponse = 즐겨찾기_삭제(favoriteIdNotExist);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * when 인증되지 않은 사용자가 즐겨찾기 경로를 등록하면
     * then 예외가 발생한다
     */
    @DisplayName("생성 API 인증 테스트")
    @Test
    void createFavoriteWithAuth() {
        // when
        var body = new HashMap<>();
        body.put("source", 교대역);
        body.put("target", 강남역);

        var response = RestAssured.
                given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/favorites")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * when 인증되지 않은 사용자가 즐겨찾기 경로를 삭제하면
     * then 예외가 발생한다
     */
    @DisplayName("삭제 API 인증 테스트")
    @Test
    void deleteFavoriteWithAuth() {
        // when
        var favoriteId = 123L;
        var response = RestAssured
                .given()
                    .pathParam("id", favoriteId)
                .when()
                    .delete("/favorites/{id}")
                .then()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_등록(Long source, Long target) {
        var body = new HashMap<>();
        body.put("source", source);
        body.put("target", target);

        return MemberSteps
                .givenLogin()
                    .body(body)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                    .post("/favorites")
                .then().log().all()
                    .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회() {
        return MemberSteps
                .givenLogin()
                .when().log().all()
                    .get("/favorites")
                .then().log().all()
                    .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제(Long favoriteId) {
        return MemberSteps
                .givenLogin()
                    .pathParam("id", favoriteId)
                .when()
                    .delete("/favorites/{id}")
                .then()
                    .extract();
    }
}
