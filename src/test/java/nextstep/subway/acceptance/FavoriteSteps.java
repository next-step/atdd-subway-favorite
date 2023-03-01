package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.net.URI;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성(String token, Long source, Long target) {
        Map<String, Long> params = Map.of("source", source, "target", target);
        return RestAssured
                .given().log().all()
                .header(new Header("authorization", token))
                .contentType(ContentType.JSON)
                .accept(ContentType.ANY)
                .body(params)
                .when().log().all()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String token) {
        return RestAssured
                .given().log().all()
                .header(new Header("authorization", token))
                .accept(ContentType.JSON)
                .when().log().all()
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String token, String uri) {
        return RestAssured
                .given().log().all()
                .header(new Header("authorization", token))
                .accept(ContentType.ANY)
                .when().log().all()
                .delete(URI.create(uri))
                .then().log().all()
                .extract();
    }

}
