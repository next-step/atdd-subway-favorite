package nextstep.subway.acceptance.favorite;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.common.CommonSteps.*;

public class FavoriteTestUtils {

    private static final String BASE_URL = "/favorites";

    private FavoriteTestUtils() {}

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        ExtractableResponse<Response> getResponse = RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL)
                .then().log().all()
                .extract();
        return getResponse;
    }

    public static List<FavoriteResponse> 즐겨찾기_조회_요청_성공(String accessToken) {
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);
        checkHttpResponseCode(response, HttpStatus.OK);
        return response.jsonPath().getList("$", FavoriteResponse.class);
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
                .post(BASE_URL)
                .then().log().all()
                .extract();
        return postResponse;
    }

    public static String 즐겨찾기_등록_요청_성공(String accessToken, String sourceStationId, String targetStationId) {
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, sourceStationId, targetStationId);
        checkHttpResponseCode(response, HttpStatus.CREATED);
        return response.header("Location");
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String favoriteLocationUrl) {
        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .when()
                .delete(favoriteLocationUrl)
                .then()
                .extract();
        return deleteResponse;
    }

    public static void 즐겨찾기_삭제_요청_성공(String accessToken, String favoriteLocationUrl) {
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, favoriteLocationUrl);
        checkHttpResponseCode(response, HttpStatus.NO_CONTENT);
    }
}
