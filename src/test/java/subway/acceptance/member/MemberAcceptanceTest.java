package subway.acceptance.member;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.acceptance.auth.AuthFixture;
import subway.acceptance.auth.AuthSteps;
import subway.utils.AcceptanceTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * When 회원가입을 하면
     * Then 회원 가입이 된다.
     */
    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        var response = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 내 정보를 조회하면
     * Then 내 정보를 조회할 수 있다
     */
    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        var memberCreateResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        String memberLocation = memberCreateResponse.response().getHeader("Location");
        var 로그인 = AuthFixture.로그인_요청_만들기(EMAIL, PASSWORD);
        var loginResponse = AuthSteps.로그인_API(로그인);
        var accessToken = loginResponse.jsonPath().getString("accessToken");

        // when
        var response = RestAssured.given().log().all()
                .header("Authorization", AuthFixture.BEARER_만들기(accessToken))
                .when().get(memberLocation)
                .then().log().all()
                .extract();

        // then
        assertThat(response.jsonPath().getString("id")).isNotBlank();
        assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
        assertThat(response.jsonPath().getString("age")).isEqualTo(String.valueOf(AGE));
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 내 정보를 수정하면
     * Then 내 정보를 수정할 수 있다
     */
    @DisplayName("회원 정보를 수정한다.")
    @Test
    void changeMyInfo() {
        // given
        var memberCreateResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        String memberLocation = memberCreateResponse.response().getHeader("Location");
        var 로그인 = AuthFixture.로그인_요청_만들기(EMAIL, PASSWORD);
        var loginResponse = AuthSteps.로그인_API(로그인);
        var accessToken = loginResponse.jsonPath().getString("accessToken");

        // when
        final String newEmail = "new@gmail.com";
        Map<String, String> params = new HashMap<>();
        params.put("email", newEmail);
        params.put("password", PASSWORD);
        params.put("age", String.valueOf(AGE));
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", AuthFixture.BEARER_만들기(accessToken))
                .when().put(memberLocation)
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        var response = RestAssured.given().log().all()
                .header("Authorization", AuthFixture.BEARER_만들기(accessToken))
                .when().get(memberLocation)
                .then().log().all()
                .extract();

        // then
        assertThat(response.jsonPath().getString("id")).isNotBlank();
        assertThat(response.jsonPath().getString("email")).isEqualTo(newEmail);
        assertThat(response.jsonPath().getString("age")).isEqualTo(String.valueOf(AGE));
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 토큰을 통해 나를 삭제 하면
     * Then 삭제 할 수 있다
     */
    @DisplayName("회원을 삭제한다.")
    @Test
    void deleteMyInfo() {
        // given
        var memberCreateResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        String memberLocation = memberCreateResponse.response().getHeader("Location");
        var 로그인 = AuthFixture.로그인_요청_만들기(EMAIL, PASSWORD);
        var loginResponse = AuthSteps.로그인_API(로그인);
        var accessToken = loginResponse.jsonPath().getString("accessToken");

        // when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", AuthFixture.BEARER_만들기(accessToken))
                .when().delete(memberLocation)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value()).extract();

        var response = RestAssured.given().log().all()
                .header("Authorization", AuthFixture.BEARER_만들기(accessToken))
                .when().get(memberLocation)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    /**
     * Given 회원 가입을 생성하고
     * When 토큰 없이 내 정보를 조회하면
     * Then 내 정보를 조회할 수 없다
     */
    @DisplayName("로그인 없이 회원 정보를 조회할 수 없다.")
    @Test
    void getMember() {
        // given
        var createResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = MemberSteps.회원_정보_조회_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    /**
     * Given 회원 가입을 생성하고
     * When 토큰 없이 내 정보를 수정하면
     * Then 내 정보를 수정할 수 없다
     */
    @DisplayName("로그인 없이 회원 정보를 수정할 수 없다.")
    @Test
    void updateMember() {
        // given
        var createResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = MemberSteps.회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 회원 가입을 생성하고
     * When 토큰 없이 회원을 삭제하면
     * Then 회원을 삭제할 수 없다
     */
    @DisplayName("로그인 없이 회원을 삭제할 수 없다.")
    @Test
    void deleteMember() {
        // given
        var createResponse = MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        var response = MemberSteps.회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}