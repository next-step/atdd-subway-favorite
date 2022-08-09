package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.ADMIN_토큰권한으로_호출;
import static nextstep.subway.acceptance.FavoriteSteps.로그인후_즐겨찾기_삭제;
import static nextstep.subway.acceptance.FavoriteSteps.로그인후_즐겨찾기_생성;
import static nextstep.subway.acceptance.FavoriteSteps.로그인후_즐겨찾기_조회;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 강남역_stationId;
    private Long 양재역_stationId;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역_stationId = 지하철역_생성_요청("강남역").jsonPath()
                .getLong("id");
        양재역_stationId = 지하철역_생성_요청("양재역").jsonPath()
                .getLong("id");
    }


    /**
     * When 즐겨찾기를 생성하고
     * Then 즐겨찾기를 모두 조회하면, 생성한 즐겨찾기가 조회목록에 포함되어 있다.
     */
    @DisplayName("즐겨찾기를 조회한다")
    @Test
    void getFavorites() {
        // When
        var createdResponse = 로그인후_즐겨찾기_생성(강남역_stationId, 양재역_stationId);

        // Then
        var getResponse = 로그인후_즐겨찾기_조회();
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath()
                .getList("source.id", Long.class)).contains(강남역_stationId);
        assertThat(getResponse.jsonPath()
                .getList("target.id", Long.class)).contains(양재역_stationId);
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 해당 즐겨찾기를 삭제했을때
     * Then 즐겨찾기를 모두 조회하면 삭제한 즐겨찾기를 찾을 수 없다.
     */
    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorites() {
        // Given
        var createResponse = 로그인후_즐겨찾기_생성(강남역_stationId, 양재역_stationId);

        // When
        var deleteResponse = 로그인후_즐겨찾기_삭제(createResponse);

        // Then
        var getResponse = 로그인후_즐겨찾기_조회();
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath()
                .getList("id", Long.class)).isEmpty();
    }

    /**
     * When 비로그인 상태에서, 즐겨찾기 생성 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("비로그인 상태에서 즐겨찾기를 생성한다")
    @Test
    void saveFavorites_fail_not_login() {
        // When
        Map<String, Long> params = new HashMap<>();
        params.put("source", 강남역_stationId);
        params.put("target", 양재역_stationId);
        var response = RestAssured.given()
                .log()
                .all()
                .body(params)
                .when()
                .post("/favorites")
                .then()
                .log()
                .all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * When 비로그인 상태에서, 즐겨찾기 조회 생성 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("비로그인 상태에서 즐겨찾기를 조회한다")
    @Test
    void getFavorites_fail_not_login() {
        // When
        var response = RestAssured.given()
                .log()
                .all()
                .when()
                .get("/favorites")
                .then()
                .log()
                .all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 비로그인 상태에서,
     * When 즐겨찾기 조회 삭제 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("비로그인 상태에서 즐겨찾기를 삭제한다")
    @Test
    void deleteFavorites_fail_not_login() {
        // When
        var response = RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/favorites/{favoriteId}", 1L)
                .then()
                .log()
                .all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * When 즐겨찾기를 생성할 때, 없는 지하철역 값을 전달하면
     * Then 오류가 발생한다.
     */
    @DisplayName("즐겨찾기 생성시 없는 지하철역 값을 전달한다")
    @Test
    void saveFavorites_fail_not_exist_station() {
        // When
        var createResponse = 로그인후_즐겨찾기_생성(999L, 강남역_stationId);

        // Then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 즐겨찾기를 삭제할 때, 없는 지하철역 값을 전달하면
     * Then 오류가 발생한다.
     */
    @DisplayName("즐겨찾기 삭제시 없는 지하철역 값을 전달한다")
    @Test
    void Favorites_fail_not_exist_station() {
        // When
        var response = ADMIN_토큰권한으로_호출()
                .when()
                .delete("/favorites/9999")
                .then()
                .log()
                .all()
                .extract();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
