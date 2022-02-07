package nextstep.subway.commons;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class RestAssuredUtils {

    private RestAssuredUtils() {}

    public static ExtractableResponse<Response> get(String url) {
        return RestAssured
                .given().log().all()
                .when().get(url)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> post(String url, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(url)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> put(String url, Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(url)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> delete(String url) {
        return RestAssured
                .given().log().all()
                .when().delete(url)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> getWithToken(String url, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> postWithToken(String url, Map<String, String> params, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(url)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> putWithToken(String url, Map<String, String> params, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(url)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> deleteWithToken(String url, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(url)
                .then().log().all().extract();
    }


}
