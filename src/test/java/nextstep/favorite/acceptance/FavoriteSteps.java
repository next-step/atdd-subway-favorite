package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return given().log().all()
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long favoriteId) {
        return given().log().all()
            .header("Authorization", "Bearer " + token)
            .when().delete("/favorites/" + favoriteId)
            .then().log().all().extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    public static void 즐겨찾기_조회됨(ExtractableResponse<Response> response, String sourceName, String targetName) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> sources = response.jsonPath().getList("source.name");
        List<String> targets = response.jsonPath().getList("target.name");

        assertThat(sources).contains(sourceName);
        assertThat(targets).contains(targetName);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

