package nextstep.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class Request {
    public static ExtractableResponse<Response> oauthGet(final String token, final String path) {
        return oauthGet(token, path, MediaType.APPLICATION_JSON_VALUE, new HashMap<>());
    }

    public static ExtractableResponse<Response> oauthGet(final String token,
                                                         final String path,
                                                         final Map<String, ?> params) {
        return oauthGet(token, path, MediaType.APPLICATION_JSON_VALUE, params);
    }

    public static ExtractableResponse<Response> oauthGet(final String token,
                                                         final String contentType,
                                                         final String path,
                                                         final Map<String, ?> params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(contentType)
                .queryParams(params)
                .when().get(path)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> oauthPost(final String token, final String path, final Map<String, ?> body) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(path)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> oauthDelete(final String token, final String path) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(path)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> post(final Map<String, ?> headers,
                                                     final String contentType,
                                                     final String path,
                                                     final Map<String, ?> params) {
        return RestAssured
                .given().log().all()
                .headers(headers)
                .contentType(contentType)
                .body(params)
                .when().post(path)
                .then().log().all().extract();
    }
}
