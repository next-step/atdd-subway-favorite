package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성(String source, String destination, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", destination);
        return RestAssured
                .given().log().all()
                .body(params)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(long deleteId, String accessToken) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites" + "/" + deleteId)
                .then().log().all().extract();
    }

    public static void 즐겨찾기_조회_검증(ExtractableResponse<Response> response, List<String> sources, List<String> targets) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("source.name", String.class))
                .containsOnly(sources.toArray(sources.toArray(new String[0])));
        assertThat(response.jsonPath().getList("target.name", String.class))
                .containsOnly(targets.toArray(sources.toArray(new String[0])));
    }
}
