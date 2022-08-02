package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_삭제_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private static final String MEMBER_EMAIL = "member@email.com";
    private static final String MEMBER_PASSWORD = "password2";
    private static final Integer MEMBER_AGE = 17;

    /**
     * When : 이메일, 비밀번호를 통해 베이직 인증로 해당 회원 정보를 요청하면
     * Then : 회원 정보가 조회된다
     */

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        //When
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        //Then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
     * When : 이메일 비밀번호를 통해 폼 로그인 인증으로 해당 회원 정보를 요청하면
     * Then : 회원 정보가 조회된다
     */

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        //When
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        //Then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
     * Given : 이메일, 비밀번호를 통해 로그인 후 토큰이 생성되었고
     * When  : 해당 토큰으로 정보 조회를 요청하면
     * Then  : 회원 정보가 조회된다
     */

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        //When
        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        //Then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    /**
     * Given : 관리자로 로그인이 되어 있을 때
     * When  : 괸리자만 접근 가능한 지하철 생성 요청을 하면
     * Then  : 지하철역이 생성된다.
     */
    @DisplayName("관리자로 지하철역 생성")
    @Test
    void enterCreateUrlOnlyAdmin() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        //When
        ExtractableResponse<Response> response = 지하철역_생성_요청(accessToken, "마곡역");

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given : 관리자로 로그인이 되어 있을 때, 지하철역을 생성했을때,
     * When  : 괸리자만 접근 가능한 지하철 삭제 요청을 하면
     * Then  : 지하철역이 삭제된다.
     */
    @DisplayName("관리자로 지하철역 삭제")
    @Test
    void enterDeleteUrlOnlyAdmin() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(accessToken, "마곡역");

        //When
        ExtractableResponse<Response> response = 지하철역_삭제_요청(accessToken, createResponse.header("location"));

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given : 잘못된 accessToken이 주어졌을때
     * When  : 해당 토큰으로 정보 조회를 요청하면
     * Then  : 회원 정보가 조회되지 않는다.
     */

    @DisplayName("잘못된 토큰 정보")
    @Test
    void inputInvalidToken() {
        //Given
        String accessToken = "잘못된 토큰입니다.";

        //When Then
        회원_정보가_조회되지_않음(accessToken);

    }

    /**
     * Given : 멤버로 로그인이 되어 있을 때
     * When  : 괸리자만 접근 가능한 지하철 생성 요청을 하면
     * Then  : 해당 요청이 거부된다.
     */
    @DisplayName("멤버로 지하철역 생성 불가")
    @Test
    void doNotCreateStationForMember() {
        //Given
        String accessToken = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);

        //When
        ExtractableResponse<Response> response = 지하철역_생성_요청(accessToken, "마곡역");

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given : 관리자로 로그인이 되어 있을 때, 지하철역을 생성하고 멤버로 로그인 했을 떄
     * When  : 괸리자만 접근 가능한 지하철 삭제 요청을 하면
     * Then  : 지하철역이 삭제된다.
     */
    @DisplayName("관리자만 접근 가능한 URL(삭제)")
    @Test
    void doNotDeleteStationForMember() {
        //Given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(accessToken, "마곡역");
        String memberToken = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);

        //When
        ExtractableResponse<Response> response = 지하철역_삭제_요청(memberToken, createResponse.header("location"));

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured
                .given().log().all()
                .auth().form(email, password, new FormAuthConfig("/login/form", USERNAME_FIELD, PASSWORD_FIELD))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
