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
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(email, password, age))
                .when().post("/members")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(final ExtractableResponse<Response> response) {
        final String uri = response.header("Location");

        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_정보_조회_요청(final String token) {
        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(final ExtractableResponse<Response> response,
                                                            final String email,
                                                            final String password,
                                                            final Integer age
    ) {
        final String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(email, password, age))
                .when().put(uri)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(final ExtractableResponse<Response> response) {
        final String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all().extract();
    }

    public static void 회원_정보_조회됨(final ExtractableResponse<Response> response, final String email, final int age) {
        assertAll(
                () -> assertThat(response.jsonPath().getString("id")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("email")).isEqualTo(email),
                () -> assertThat(response.jsonPath().getInt("age")).isEqualTo(age)
        );
    }
}
