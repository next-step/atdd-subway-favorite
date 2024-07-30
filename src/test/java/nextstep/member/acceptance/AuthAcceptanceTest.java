package nextstep.member.acceptance;

import static nextstep.member.acceptance.AuthSteps.*;
import static nextstep.member.application.dto.GithubResponses.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@example.com";
    public static final String PASSWORD = "default_password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        // when
        ExtractableResponse<Response> response = 로그인_요청(params);

        // then
        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        // when
        ExtractableResponse<Response> response2 = 회원_정보_조회_요청(accessToken);

        // then
        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    private static ExtractableResponse<Response> 회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 토큰을 생성하려고 하면 오류가 발생한다")
    void createToken_MemberNotFound() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "non-existent@example.com");
        params.put("password", "password");

        ExtractableResponse<Response> response = 로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("틀린 비밀번호로 토큰 생성을 요청하면 오류가 발생한다")
    void createToken_InvalidPassword() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", "wrong_password");

        ExtractableResponse<Response> response = 로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Github 로그인을 통해 토큰을 발급받는다.")
    void githubAuth() {
        Map<String, String> params = new HashMap<>();
        params.put("code", 사용자1.getCode());

        ExtractableResponse<Response> response = github_로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Test
    @DisplayName("github를 통해 토큰을 생성하려고 할 때 존재하지 않는 회원에 대해 회원이 가입되고 토큰이 생성된다")
    void createTokenFromGithub_NewMember() {
        String code = 사용자2.getCode();
        String email = 사용자2.getEmail();

        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = github_로그인_요청(params);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();

        Member member = memberRepository.findByEmail(email).orElseThrow();
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("잘못된 코드 값으로 Github를 통해 토큰을 생성하려고 할 때 오류가 발생한다")
    void createTokenFromGithub_InvalidAccessToken() {
        Map<String, String> params = new HashMap<>();
        params.put("code", "invalid_code");

        ExtractableResponse<Response> response = github_로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}