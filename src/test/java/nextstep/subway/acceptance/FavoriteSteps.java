package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    private static RequestSpecification bearerAuthRequest(String token) {
        return RestAssured.given().log().all()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());

        return bearerAuthRequest(token)
            .body(params)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return bearerAuthRequest(token)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long id) {
        return bearerAuthRequest(token)
            .pathParam("id", id)
            .when().delete("/favorites/{id}")
            .then().log().all()
            .extract();
    }
}
