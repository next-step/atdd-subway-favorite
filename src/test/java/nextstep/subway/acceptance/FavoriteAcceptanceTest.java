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
        지하철역_생성_요청("교대역", ACCESS_TOKEN);
        지하철역_생성_요청("남부터미널역", ACCESS_TOKEN);
        지하철역_생성_요청("양재역", ACCESS_TOKEN);

        //when
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(1L, 3L);

        //then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        assertThat(getResponse.jsonPath().getList("source.name", String.class).get(0)).isEqualTo("교대역");
        assertThat(getResponse.jsonPath().getList("target.name", String.class).get(0)).isEqualTo("양재역");
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
}
