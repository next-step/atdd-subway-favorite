package nextstep.auth.acceptance.model;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public final class MemberEntitiesHelper {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private static final String MEMBER_URI = "/members";
    private static final String MEMBER_ME_URI = "/members/me";
    private static final String TOKEN_LOGIN_URI = "/login/token";
    private static final String SESSION_LOGIN_URI = "/login/session";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    public static String 로그인_되어_있음() {
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        return response.jsonPath().getString("accessToken");
    }

    private static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(TOKEN_LOGIN_URI)
                .then().log().all()
                .statusCode(OK.value()).extract();
    }

    public static ExtractableResponse<Response> 회원가입을_한다() {
        ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        return response;
    }

    public static void 회원_정보를_조회한다(ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);
        회원_정보_조회됨(response);
    }

    public static void 회원_정보를_수정_한다(ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    public static void 회원_정보를_삭제_한다(ExtractableResponse<Response> createResponse) {
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> 회원_생성_요청(String email, String password, Integer age) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age);

        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(MEMBER_URI)
                .then().log().all().extract();
    }

    private static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given().log().all()
                .accept(APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청() {
        return RestAssured
                .given().log().all()
                .auth().form(EMAIL, PASSWORD, new FormAuthConfig(SESSION_LOGIN_URI, USERNAME_FIELD, PASSWORD_FIELD))
                .accept(APPLICATION_JSON_VALUE)
                .when().get(MEMBER_ME_URI)
                .then().log().all()
                .statusCode(OK.value()).extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(APPLICATION_JSON_VALUE)
                .when().get(MEMBER_ME_URI)
                .then().log().all()
                .statusCode(OK.value())
                .extract();
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(AGE);
    }

    public static void 내_회원_정보를_수정_한다(String accessToken) {
        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(accessToken, "new" + EMAIL, "new" + PASSWORD, AGE);
        assertThat(updateResponse.statusCode()).isEqualTo(OK.value());
    }

    public static void 내_회원_정보를_삭제_한다(String accessToken) {
        ExtractableResponse<Response> deleteResponse = 내_회원_정보_삭제_요청(accessToken);
        assertThat(deleteResponse.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(uri)
                .then().log().all().extract();
    }

    private static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all().extract();
    }

    private static ExtractableResponse<Response> 내_회원_정보_수정_요청(String accessToken, String email, String password, Integer age) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(MEMBER_ME_URI)
                .then().log().all().extract();
    }

    private static ExtractableResponse<Response> 내_회원_정보_삭제_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(MEMBER_ME_URI)
                .then().log().all().extract();
    }
}