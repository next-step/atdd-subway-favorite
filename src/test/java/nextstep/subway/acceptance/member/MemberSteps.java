package nextstep.subway.acceptance.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.auth.AuthSteps.ADMIN_EMAIL;
import static nextstep.subway.acceptance.auth.AuthSteps.ADMIN_PASSWORD;
import static nextstep.subway.utils.RestAssuredUtils.given;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberSteps {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    public static String 로그인_되어_있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        return response.jsonPath().getString("accessToken");
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = Map.of(
                "email", email,
                "password", password
        );
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password, Integer age) {
        Map<String, String> params = Map.of(
                "email", email,
                "password", password,
                "age", String.valueOf(age)
        );
        return given(로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(response.header("Location"))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header("Location");

        Map<String, String> params = Map.of(
                "email", email,
                "password", password,
                "age", String.valueOf(age)
        );

        return given(로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(uri)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        return given(로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD))
                .when().delete(response.header("Location"))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(String accessToken) {
        return given(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_수정_요청(String accessToken, String email, String password, Integer age) {
        Map<String, String> params = Map.of(
                "email", email,
                "password", password,
                "age", String.valueOf(age)
        );

        return given(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_삭제_요청(String accessToken) {
        return given(accessToken)
                .when().delete("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
    }

    public static void 인증_예외_발생(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}