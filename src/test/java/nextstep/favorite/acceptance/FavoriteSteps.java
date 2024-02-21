package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.acceptance.AuthSteps.createToken;

public class FavoriteSteps {

    public static final String FAVORITES_URL = "/favorites";

    private FavoriteSteps() {

    }

    public static ExtractableResponse<Response> 즐겨찾기를_등록한다(final String email, final Long source, final Long target) {
        final String jwtToken = createToken(email);
        Map<String, String> params = createParams(source, target);

        return RestAssured.given().log().all()
                .auth().oauth2(jwtToken)
                .body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITES_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토근_없이_즐겨찾기를_등록한다(final Long source, final Long target) {
        Map<String, String> params = createParams(source, target);
        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(FAVORITES_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기를_조회한다(final String email) {
        final String token = createToken(email);
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(FAVORITES_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기를_삭제한다(final String email, final String locationUrl) {
        final String token = createToken(email);
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(locationUrl)
                .then().log().all()
                .extract();
    }

    private static Map<String, String> createParams(final Long source, final Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));
        return params;
    }

}
