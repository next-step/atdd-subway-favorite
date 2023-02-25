package nextstep.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.fixture.AuthFixture;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static nextstep.fixture.AuthFixture.로그인된_상태;
import static nextstep.fixture.FieldFixture.식별자_아이디;
import static nextstep.fixture.FieldFixture.회원_나이;
import static nextstep.fixture.FieldFixture.회원_이메일;
import static nextstep.utils.JsonPathUtil.Integer로_추출;
import static nextstep.utils.JsonPathUtil.문자열로_추출;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberSteps {

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

    public static ExtractableResponse<Response> 베이직_인증으로_내_회원_정보_조회_요청(String username, String password) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(username, password)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(AuthFixture 인증_주체) {
        return given(로그인된_상태(인증_주체)).log().all()
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        assertThat(response.jsonPath().getString("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(email);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(age);
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> 내_정보_조회_결과, AuthFixture 인증_주체) {
        assertThat(문자열로_추출(내_정보_조회_결과, 식별자_아이디)).isNotNull();
        assertThat(문자열로_추출(내_정보_조회_결과, 회원_이메일)).isEqualTo(인증_주체.회원_정보().이메일());
        assertThat(Integer로_추출(내_정보_조회_결과, 회원_나이)).isEqualTo(인증_주체.회원_정보().나이());
    }

    public static void AccessToken이_JWT_토큰_형식으로_반환된다(ExtractableResponse<Response> 로그인_요청_결과) {
        assertThat(로그인_요청_결과.jsonPath().getString("accessToken").split("\\.")).hasSize(3);
    }

    public static void 로그인이_성공한다(ExtractableResponse<Response> 로그인_요청_결과) {
        assertThat(로그인_요청_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 내_정보_조회가_성공한다(ExtractableResponse<Response> 내_정보_조회_결과) {
        assertThat(내_정보_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
