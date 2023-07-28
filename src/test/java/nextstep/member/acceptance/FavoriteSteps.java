package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> location = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .delete(createResponse.header("Location"))
                .then().log().all()
                .extract();

        return location;
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", "" + source);
        params.put("target", "" + target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> location = RestAssured
                .given().log().all()
                .when()
                .delete(createResponse.header("Location"))
                .then().log().all()
                .extract();

        return location;
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청() {
        return RestAssured
                .given().log().all()
                .when()
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", "" + source);
        params.put("target", "" + target);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }
}
