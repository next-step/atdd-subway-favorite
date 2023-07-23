package nextstep.api.member.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.api.member.application.dto.MemberRequest;

public class MemberSteps {
    public static ExtractableResponse<Response> 회원_생성_요청(final String email, final String password, final Integer age) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(email, password, age))
                .when().post("/members")
                .then().extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(final ExtractableResponse<Response> response) {
        final String uri = response.header("Location");

        return RestAssured.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(final String token) {
        return RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(final ExtractableResponse<Response> response,
                                                            final String email,
                                                            final String password,
                                                            final Integer age
    ) {
        final String uri = response.header("Location");
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(email, password, age))
                .when().put(uri)
                .then().extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(final ExtractableResponse<Response> response) {
        final String uri = response.header("Location");
        return RestAssured
                .given()
                .when().delete(uri)
                .then().extract();
    }

    public static void 회원_정보_조회됨(final ExtractableResponse<Response> response, final String email, final int age) {
        assertAll(
                () -> assertThat(response.jsonPath().getString("id")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("email")).isEqualTo(email),
                () -> assertThat(response.jsonPath().getInt("age")).isEqualTo(age)
        );
    }
}
