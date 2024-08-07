package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Map<String, Object> request) {
        return RestAssured.given().log().all()
                .header("Authorization", request.get("authorization"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(Map<String, Object> request) {
        return RestAssured.given().log().all()
                .header("Authorization", request.get("authorization"))
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Map<String, Object> request) {
        return RestAssured.given().log().all()
                .header("Authorization", request.get("authorization"))
                .when().delete("/favorites/" + request.get("id"))
                .then().log().all()
                .extract();

    }
}
