package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.acceptance.AuthSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
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

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        ExtractableResponse<Response> tokenResponse = 깃허브_토큰_발급(GithubResponses.사용자3.getCode());
        GithubAccessTokenResponse githubAccessTokenResponse = tokenResponse.as(GithubAccessTokenResponse.class);

        assertThat(githubAccessTokenResponse.getAccessToken()).isEqualTo(GithubResponses.사용자3.getAccessToken());
    }

    @DisplayName("Github Profile 조회")
    @Test
    void githubProfile() {
        ExtractableResponse<Response> tokenResponse = 깃허브_토큰_발급(GithubResponses.사용자3.getCode());
        GithubAccessTokenResponse githubAccessTokenResponse = tokenResponse.as(GithubAccessTokenResponse.class);

        // then
        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // token 을 이용해서 정보 조회를 했을 때
        ExtractableResponse<Response> profileResponse = 깃허브_정보_조회(githubAccessTokenResponse.getAccessToken());
        GithubProfileResponse githubProfileResponse = profileResponse.as(GithubProfileResponse.class);

        assertThat(githubProfileResponse.getEmail()).isEqualTo(GithubResponses.사용자3.getEmail());
        assertThat(githubProfileResponse.getAge()).isEqualTo(GithubResponses.사용자3.getAge());
    }

    @DisplayName("Github Login")
    @Test
    void githubLogin() {
        ExtractableResponse<Response> response = 깃허브_로그인_요청(GithubResponses.사용자1.getCode());

        // then 토큰 발급 성공
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Login 실패")
    @Test
    void githubLoginFail() {
        final ExtractableResponse<Response> response = 깃허브_로그인_요청(GithubResponses.사용자_인증X.getCode());

        // then 토큰 발급 실패
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
