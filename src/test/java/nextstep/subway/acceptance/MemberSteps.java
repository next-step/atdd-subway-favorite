package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.config.message.AuthError;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.config.message.MemberError.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberSteps {

    public static ExtractableResponse<Response> 베어러_인증_로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/members")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(final String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 베이직_인증으로_내_회원_정보_조회_요청(String username, String password) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(username, password)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
    }

    public static void 내_회원_정보_조회_실패(ExtractableResponse<Response> 내_회원_정보_조회_응답, final HttpStatus httpStatus, final AuthError authError) {
        final JsonPath jsonPathResponse = 내_회원_정보_조회_응답.response().body().jsonPath();
        assertAll(
                () -> assertThat(내_회원_정보_조회_응답.statusCode()).isEqualTo(httpStatus.value()),
                () -> assertThat(jsonPathResponse.getString("message")).isEqualTo(authError.getMessage())
        );
    }

    public static void 내_회원_정보_조회_성공(final ExtractableResponse<Response> 내_회원_정보_조회_응답, final String email, final Integer age) {
        final JsonPath jsonPathResponse = 내_회원_정보_조회_응답.response().body().jsonPath();
        assertAll(
                () -> assertThat(내_회원_정보_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPathResponse.getString("id")).isNotNull(),
                () -> assertThat(jsonPathResponse.getString("email")).isEqualTo(email),
                () -> assertThat(jsonPathResponse.getInt("age")).isEqualTo(age)
        );

    }

    public static String 로그인_되어_있음(final ExtractableResponse<Response> response) {
        final TokenResponse tokenResponse = response.as(TokenResponse.class);
        return tokenResponse.getAccessToken();
    }

    public static void 인증_로그인_응답_성공(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("accessToken")).isNotBlank()
        );
    }

    public static void 인증_로그인_응답_실패(final ExtractableResponse<Response> response) {

        final JsonPath jsonPathResponse = response.response().body().jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(jsonPathResponse.getString("message")).isEqualTo(UNAUTHORIZED.getMessage())
        );
    }
}