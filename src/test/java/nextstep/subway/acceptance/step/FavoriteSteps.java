package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(Long sourceId, Long targetId, String accessToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static void 단일_즐겨찾기_정보_조회됨(ExtractableResponse<Response> response, String source, String target) {
        assertThat(response.jsonPath().getLong("id")).isNotNull();
        assertThat(response.jsonPath().getString("source.name")).isEqualTo(source);
        assertThat(response.jsonPath().getString("target.name")).isEqualTo(target);
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, Long... favoriteIds) {
        assertThat(response.jsonPath().getList("id", Long.class)).containsExactly(favoriteIds);
    }
}