package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    private String accessToken;
    private String otherAccessToken;

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

        지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 3);

        회원_생성_요청("email@email.com", "password", 20);
        accessToken = 로그인_되어_있음("email@email.com", "password");

        회원_생성_요청("other@email.com", "password", 21);
        otherAccessToken = 로그인_되어_있음("other@email.com", "password");
    }

    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void createFavorite() {
        // when
        final ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("즐겨찾기 목록을 조회한다")
    @Test
    void findAllFavorite() {
        // given
        즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);
        즐겨찾기_생성_요청(accessToken, 양재역, 강남역);
        즐겨찾기_생성_요청(accessToken, 교대역, 양재역);
        즐겨찾기_생성_요청(accessToken, 남부터미널역, 교대역);

        // when
        final ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(강남역, 양재역, 교대역, 남부터미널역),
                () -> assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(남부터미널역, 강남역, 양재역, 교대역)
        );
    }

    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {
        // given
        final ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);

        // when
        final ExtractableResponse<Response> response = 즐겨_찾기_삭제_요청(accessToken, createResponse.header("Location"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("다른 사람의 즐겨찾기를 삭제할  수 없다")
    @Test
    void deleteFavoriteByOther() {
        // given
        final ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(otherAccessToken, 강남역, 남부터미널역);

        // when
        final ExtractableResponse<Response> response = 즐겨_찾기_삭제_요청(accessToken, createResponse.header("Location"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 즐겨_찾기_삭제_요청(final String accessToken, final String locationUrl) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.ALL_VALUE)
                .when().delete(locationUrl)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(final String accessToken, final Long source, final Long target) {
        final Map<String, String> params = 즐겨찾기_생성_데이터를_만든다(source, target);
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.ALL_VALUE)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private Map<String, String> 즐겨찾기_생성_데이터를_만든다(final Long source, final Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());
        return params;
    }
}
