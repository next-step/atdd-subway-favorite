package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String ROOT_PATH = "/favorites";


    /**
     * Given 역을 미리 추가해놓고
     * When 즐겨찾기를 추가하면
     * Then 즐겨찾기 조회 시 추가된 즐겨찾기 역들을 확인할 수 있다
     */
    @DisplayName("즐겨찾기 추가")
    @Test
    void createFavorite() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(교대역, 양재역);

        //then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        assertThat(getResponse.jsonPath().getList("source.name", String.class).get(0)).isEqualTo("교대역");
        assertThat(getResponse.jsonPath().getList("target.name", String.class).get(0)).isEqualTo("양재역");
    }


    /**
     * Given 역과 즐겨찾기를 추가해놓고
     * When 즐겨찾기를 조회하면
     * Then 추가된 즐겨찾기 역들을 확인할 수 있다
     */
    @DisplayName("즐겨찾기 조회")
    @Test
    void showFavorites() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");


        즐겨찾기_생성_요청(교대역, 양재역);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.jsonPath().getList("source.name", String.class).get(0)).isEqualTo("교대역");
        assertThat(response.jsonPath().getList("target.name", String.class).get(0)).isEqualTo("양재역");
    }

    /**
     * Given 역과 즐겨찾기를 추가해놓고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기 조회 시 삭제됨을 확인할 수 있다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");


        Long 즐겨찾기 = 즐겨찾기_생성_요청(교대역, 양재역).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> deletedResponse = 즐겨찾기_목록_삭제_요청(즐겨찾기);

        //then
        assertThat(deletedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        assertThat(getResponse.jsonPath().getList("")).isEmpty();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(long source, long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");
        return RestGivenWithOauth2.from(ACCESS_TOKEN)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(ROOT_PATH)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get(ROOT_PATH)
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_삭제_요청(long id) {
        return RestGivenWithOauth2.from(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(ROOT_PATH + "/" + id)
                .then().log().all().extract();
    }
}
