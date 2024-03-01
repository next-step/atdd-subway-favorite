package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.GithubResponse.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String PASSWORD = "password";

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();

        memberRepository.save(new Member(사용자1.getEmail(), PASSWORD, 사용자1.getAge()));
        memberRepository.save(new Member(사용자2.getEmail(), PASSWORD, 사용자2.getAge()));
        memberRepository.save(new Member(사용자3.getEmail(), PASSWORD, 사용자3.getAge()));
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        Map<String, String> params = new HashMap<>();
        params.put("email", 사용자1.getEmail());
        params.put("password", PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response2.jsonPath().getString("email")).isEqualTo(사용자1.getEmail());
    }

    @DisplayName("유효하지 않은 토큰일시 에러가 발생한다.")
    @Test
    void 유효하지_않은_토큰_호출_실패() {
        String 유효하지_않은_토큰 = "invalidToken";

        RestAssured.given().log().all()
            .auth().oauth2(유효하지_않은_토큰)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value()).extract();
    }

    @DisplayName("비로그인 시 에러가 발생한다.")
    @Test
    void 비로그인_호출_실패() {
        RestAssured.given().log().all()
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value()).extract();
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        Map<String, String> params = new HashMap<>();
        params.put("code", 사용자1.getCode());

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/github")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}