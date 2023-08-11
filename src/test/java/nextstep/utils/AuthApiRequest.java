package nextstep.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthApiRequest {
    public static ExtractableResponse<Response> get(String accessToken, String path) {
        return RestAssured.given()
                .auth().preemptive().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String accessToken, String path, Object params) {
        return RestAssured.given()
                .body(params)
                .auth().preemptive().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(String accessToken, String path, Object params) {
        return RestAssured.given()
                .body(params)
                .auth().preemptive().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String accessToken, String path) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .when()
                .delete(path)
                .then()
                .log().all()
                .extract();
    }
}
