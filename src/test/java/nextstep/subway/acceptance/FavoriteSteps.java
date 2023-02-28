package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(ExtractableResponse<Response> 깃허브_로그인_응답, Long sourceId, Long targetId) {
        String accessToken = 깃허브_로그인_응답.jsonPath().getString("accessToken");

        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "token " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(
            ExtractableResponse<Response> 깃허브_로그인_응답,
            ExtractableResponse<Response> 즐겨찾기_등록_응답) {

        String accessToken = 깃허브_로그인_응답.jsonPath().getString("accessToken");
        String url = 즐겨찾기_등록_응답.jsonPath().getString(HttpHeaders.LOCATION);

        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "token " + accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }
}
