package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.OAuth2ProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.acceptance.AuthSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    /**
     * Given 사용자가
     * When github 로그인을 시도하면
     * Then accessToken를 발급한다.
     */
    @DisplayName("깃허브 로그인을 시도하면 accessToken을 발급한다")
    @Test
    void loginToGithub() {
        //given
        GithubResponses res = GithubResponses.사용자1;
        MemberSteps.회원_생성_요청(res.getEmail(), null, res.getAge());

        //when
        TokenResponse response = 깃허브_로그인_요청(res.getCode())
                .as(TokenResponse.class);

        //then
        assertThat(response.getAccessToken()).isNotBlank();
    }

    /**
     * Given 사용자 코드로 정보를 accessToken을 발급받고,
     * When 해당 토큰으로 깃허브 정보 조회를 하면
     * Then gitHubProfile 정보를 조회할 수 있다.
     */
    @DisplayName("깃허브 프로필을 조회한다.")
    @Test
    void findGithubProfile() {
        GithubAccessTokenResponse accessTokenRes = 깃허브_토큰_발급(GithubResponses.사용자1.getCode())
                .as(GithubAccessTokenResponse.class);

        OAuth2ProfileResponse githubProfileResponse = 깃허브_정보_조회(accessTokenRes.getAccessToken())
                .as(OAuth2ProfileResponse.class);

        assertThat(githubProfileResponse.getEmail()).isEqualTo(GithubResponses.사용자1.getEmail());
        assertThat(githubProfileResponse.getAge()).isEqualTo(GithubResponses.사용자1.getAge());
    }
}