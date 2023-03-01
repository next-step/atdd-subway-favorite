package nextstep.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.fixture.AuthFixture;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthSteps {

    public static ExtractableResponse<Response> 베어러_인증_로그인_요청(AuthFixture 인증_주체) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(인증_주체.회원_정보().로그인_요청_데이터_생성())
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    public static ExtractableResponse<Response> Github_로그인_요청(AuthFixture 인증_주체) {
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(인증_주체.Github_로그인_요청_데이터_생성())
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }

    public static void 로그인이_성공한다(ExtractableResponse<Response> 로그인_요청_결과) {
        assertThat(로그인_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void AccessToken이_반환된다(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
