package nextstep.subway.member.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceStep {
    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, Integer age) {
        return 회원_생성을_요청(email, password, age);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/members").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청() {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                put("/members/me").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청() {
        return RestAssured.given().log().all().
                when().
                delete("/members/me").
                then().
                log().all().
                extract();
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
