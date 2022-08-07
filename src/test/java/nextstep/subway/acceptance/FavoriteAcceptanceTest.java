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

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String ROOT_PATH = "/favorites";


    /**
     * When 즐겨찾기를 추가하면
     * Then 즐겨찾기 조회 시 추가된 즐겨찾기 역들을 확인할 수 있다
     */
    @DisplayName("즐겨찾기 추가")
    @Test
    void createFavorite() {
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
