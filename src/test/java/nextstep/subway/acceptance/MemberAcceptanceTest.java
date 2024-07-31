package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 내 정보를 조회하면
     * Then 내 정보를 조회할 수 있다
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String 로그인_토큰 = 로그인_토큰_생성(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> 내_정보_조회_응답 = 내_정보_조회(로그인_토큰);

        JsonPath 내_정보_조회_JsonPath = 내_정보_조회_응답.jsonPath();
        String email = 내_정보_조회_JsonPath.getString("email");
        int age = 내_정보_조회_JsonPath.getInt("age");

        assertThat(email).isEqualTo(EMAIL);
        assertThat(age).isEqualTo(AGE);
    }

    @DisplayName("비 로그인 상태에서 내 정보조회 시 401 Unauthorized 응답")
    @Test
    void 비_로그인_내_정보_조회() {
        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 비로그인_내_정보_조회_요청();

        //then
        assertThat(내_정보_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("유효하지 않는 토큰으로 내 정보조회 시 401 Unauthorized 응답")
    @Test
    void 유효하지_않는_토큰_내_정보_조회() {
        // when
        ExtractableResponse<Response> 내_정보_조회_응답 = 유효하지_않는_토큰_내_정보_조회_요청();

        //then
        assertThat(내_정보_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static ExtractableResponse<Response> 유효하지_않는_토큰_내_정보_조회_요청() {
        return RestAssured.given().log().all()
                .auth().oauth2("invalid token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 비로그인_내_정보_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }
}