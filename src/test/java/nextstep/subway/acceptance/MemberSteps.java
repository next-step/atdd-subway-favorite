package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberSteps {

    public static final String ADMIN_EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    public static String 관리자Bearer토큰() {
        ExtractableResponse<Response> response = 로그인_요청(ADMIN_EMAIL, PASSWORD);
        return response.jsonPath().getString("accessToken");
    }

    public static String 사용자Bearer토큰(final String email) {
        ExtractableResponse<Response> response = 로그인_요청(email, PASSWORD);
        return response.jsonPath().getString("accessToken");
    }

    public static RequestSpecification adminGiven(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    public static ExtractableResponse<Response> 회원_생성(String email, String password, Integer age) {
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

    public static Long 회원_ID_조회(final ExtractableResponse<Response> response) {
        return adminGiven(관리자Bearer토큰())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(response.header("location"))
                .then().log().all()
                .extract().jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 회원_정보_조회(final Long id) {
        return adminGiven(관리자Bearer토큰())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정(Long id, String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return adminGiven(관리자Bearer토큰())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/members/{id}", id)
                .then().log().all().extract();
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

    public static ExtractableResponse<Response> 회원_삭제(Long id) {
        return adminGiven(관리자Bearer토큰())
                .when().delete("/members/{id}", id)
                .then().log().all().extract();
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
    }
}