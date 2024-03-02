package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.acceptance.AuthSteps.깃허브_로그인_요청;
import static nextstep.member.acceptance.AuthSteps.내_정보_조회;
import static nextstep.utils.GithubResponses.사용자2;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response2 = 내_정보_조회(accessToken);

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * when 인증 정보 없이 내 정보 관리 기능을 조회하면
     * then 401 unauthorized 코드로 응답한다.
     */
    @DisplayName("error_인증 정보 없이 정보 조회")
    @Test
    void error_unauthorized() {
        RestAssured.given().log().all()
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * when 유효하지 않은 인증 정보로 내 정보 관리 기능을 조회하면
     * then 401 unauthorized 코드로 응답한다.
     */
    @DisplayName("error_유효하지 않은 인증 정보로 조회")
    @Test
    void error_unauthorized_invalid() {
        RestAssured.given().log().all()
                .auth().oauth2("qwer")
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * github 토큰 요청, 리소스 조회 요청, 사용자 저장/조회 요청 후 access token 발급
     */
    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        String accessToken = 깃허브_로그인_요청("code");
        assertThat(accessToken).isNotBlank();
    }

    /**
     * when 가입되지 않은 사용자가 github login을 시도하면
     * then accessToken으로 해당 회원 정보 조회가 가능하다.
     */
    @DisplayName("Github login_신규 회원")
    @Test
    void github_login() {
        String accessToken = 깃허브_로그인_요청(사용자2.getCode());
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response = 내_정보_조회(accessToken);
        assertThat(response.jsonPath().getString("email")).isEqualTo(사용자2.getEmail());
    }


    /**
     * given 기존 사용자 한명을 저장하고
     * when 해당 사용자가 github login을 시도하면
     * then accessToken으로 해당 회원 정보 조회가 가능하다.
     */
    @DisplayName("Github login_기존 회원")
    @Test
    void github_login_exist_user() {
        memberRepository.save(new Member(사용자2.getEmail(), null, 20));

        String accessToken = 깃허브_로그인_요청(사용자2.getCode());
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response = 내_정보_조회(accessToken);
        assertThat(response.jsonPath().getString("email")).isEqualTo(사용자2.getEmail());
    }
}
