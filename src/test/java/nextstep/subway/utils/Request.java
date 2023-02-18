package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class Request {
    public static ExtractableResponse<Response> oauthGet(final String token,
                                                         final String contentType,
                                                         final String path,
                                                         final Map<String, ?> params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(contentType)
                .body(params)
                .when().get(path)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> post(final String path, final Map<String, Object> params) {
        return post(Map.of(), path, MediaType.APPLICATION_JSON_VALUE, params);
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

    public static ExtractableResponse<Response> get(final String path, final Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(path)
                .then().log().all().extract();
    }
}
