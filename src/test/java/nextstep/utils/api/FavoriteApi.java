package nextstep.utils.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class FavoriteApi {
    public static ExtractableResponse<Response> 즐겨찾기를_추가한다(Map 추가할_즐겨찾기_바디, String 토큰) {
        return RestAssured.given().log().all()
                .body(추가할_즐겨찾기_바디)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(토큰)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기를_추가한다(Map 추가할_즐겨찾기_바디) {
        return RestAssured.given().log().all()
                .body(추가할_즐겨찾기_바디)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록을_조회한다(String 토큰) {
        return RestAssured.given().log().all()
                .auth().oauth2(토큰)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기를_삭제한다(Long 아이디, String 토큰) {
        return RestAssured.given().log().all()
                .auth().oauth2(토큰)
                .when().delete("/favorites/" + 아이디)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기를_삭제한다(Long 아이디) {
        return RestAssured.given().log().all()
                .when().delete("/favorites/" + 아이디)
                .then().log().all()
                .extract();
    }
}
