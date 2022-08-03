package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptTest extends AcceptanceTest {


    private long 교대역;
    private long 양재역;


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
    }

    /**
     * When: 로그인 후 구간이 존재하는 역을 즐겨찾기에 등록하면
     * Then: 즐겨찾기가 정상적으로 생성된다.
     */

    @Test
    @DisplayName("로그인 후 즐겨찾기가 생성된다.")
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = 로그인후_즐겨찾기_생성(교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);

    }

    /**
     * When: 로그인 하지 않고 구간이 존재하는 역을 즐겨찾기에 등록하면
     * Then: 즐겨찾기 생성이 실패한다.
     */

    @Test
    @DisplayName("로그인 하지 않으면 즐겨찾기 생성에 실패한다.")
    void createFavoriteAuthFail() {
        ExtractableResponse<Response> response = RestAssured.given()
                .body(Map.of(
                        "source", 교대역,
                        "target", 양재역
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }

    /**
     * Given: 즐겨찾기를 생성하고
     * When: 즐겨찾기를 조회하면
     * Then: 생성한 즐겨찾기가 조회된다.
     */
    @Test
    @DisplayName("즐겨찾기가 조회된다.")
    void getFavorites() {
        // given
        ExtractableResponse<Response> createResponse = 로그인후_즐겨찾기_생성(교대역, 양재역);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);

        // when
        ExtractableResponse<Response> getResponse = 로그인후_즐겨찾기_조회();

        assertAll(
                () -> assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.SC_OK),
                () -> assertThat(getResponse.jsonPath().getList("id")).hasSize(1),
                () -> assertThat(getResponse.jsonPath().getList("source.id", Long.class)).containsExactly(교대역),
                () -> assertThat(getResponse.jsonPath().getList("target.id", Long.class)).containsExactly(양재역),
                () -> assertThat(getResponse.jsonPath().getList("source.createdDate", String.class).get(0)).isNotNull(),
                () -> assertThat(getResponse.jsonPath().getList("source.modifiedDate", String.class).get(0)).isNotNull(),
                () -> assertThat(getResponse.jsonPath().getList("target.createdDate", String.class).get(0)).isNotNull(),
                () -> assertThat(getResponse.jsonPath().getList("target.modifiedDate", String.class).get(0)).isNotNull()
        );
    }

    /**
     * Given: 즐겨찾기를 생성하고
     * When: 즐겨찾기를 삭제하면
     * Then: 즐겨찾기가 삭제되고 다시 조회시 조회가 안된다.
     */
    @Test
    @DisplayName("즐겨찾기가 삭제된다.")
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 로그인후_즐겨찾기_생성(교대역, 양재역);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);

        // when
        ExtractableResponse<Response> deleteResponse = 로그인후_즐겨찾기_삭제(createResponse);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);

        // then
        ExtractableResponse<Response> getResponse = 로그인후_즐겨찾기_조회();
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(getResponse.jsonPath().getList("id")).isEmpty();
    }

}
