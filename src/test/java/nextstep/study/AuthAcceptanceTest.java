package nextstep.study;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.GithubTestRepository;
import nextstep.utils.GithubTestUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.acceptance.step.TokenSteps.깃허브_로그인_요청;
import static nextstep.auth.acceptance.step.TokenSteps.토큰_추출;
import static nextstep.member.acceptance.step.MemberSteps.내_정보_조회_요청;
import static nextstep.member.acceptance.step.MemberSteps.이메일_추출;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GithubTestRepository githubTestRepository;

    private final String userCode = "code1";
    private final String userEmail = "email1";

    @BeforeEach
    void githubSetUp() {
        githubTestRepository.cleanUp();
        githubTestRepository.addUser(userCode, new GithubTestUser(userEmail, 10));
    }

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

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When : Github 로그인을 요청하면
     * Then : AccessToken이 발급된다
     */
    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        // when
        ExtractableResponse<Response> githubLoginResponse = 깃허브_로그인_요청(userCode);

        // then
        assertThat(githubLoginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(토큰_추출(githubLoginResponse)).isNotBlank();
    }

    /**
     * Given : Github에 등록되지 않은 사용자가
     * When : Github 로그인을 요청하면
     * Then : 500 Internal server Error가 발생한다.
     */
    @DisplayName("Github Auth Fail")
    @Test
    void githubAuthFail() {
        // given
        String notRegisteredUserCode = "notRegistered";

        // when
        ExtractableResponse<Response> githubLoginResponse = 깃허브_로그인_요청(notRegisteredUserCode);

        // then
        assertThat(githubLoginResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given : Github 로그인을 요청하고
     * And : AccessToken을 발급받은 후
     * When : 내 정보 조회를 요청하면
     * Then : 조회가 성공한다.
     */
    @DisplayName("Github 인증 후 토큰으로 내정보 요청")
    @Test
    void githubAuth2() {
        // given
        String accessToken = 토큰_추출(깃허브_로그인_요청(userCode));

        // when
        ExtractableResponse<Response> response = 내_정보_조회_요청(accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        String email = 이메일_추출(response);
        assertThat(email).isEqualTo(userEmail);
    }
}