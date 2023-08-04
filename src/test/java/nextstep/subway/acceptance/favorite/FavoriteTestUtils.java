package nextstep.subway.acceptance.favorite;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class FavoriteTestUtils {
    private FavoriteTestUtils() {}

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        ExtractableResponse<Response> getResponse = RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .accept(ContentType.JSON)
                .when()
                .get("/favorites")
                .then().log().all()
                .extract();
        return getResponse;
    }

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, String sourceStationId, String targetStationId) {

        Map<String, String> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);

        ExtractableResponse<Response> postResponse = RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
        return postResponse;
    }
}
