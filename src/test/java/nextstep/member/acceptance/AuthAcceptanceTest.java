package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.acceptance.AuthStep.깃허브_로그인;
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

        ExtractableResponse<Response> response = AuthStep.로그인(EMAIL, PASSWORD);

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
        MemberSteps.회원_생성_요청(GithubResponses.사용자1.getEmail(), GithubResponses.사용자1.getCode(), 10);
        assertLogin(GithubResponses.사용자1.getCode());
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth__회원가입() {
        assertLogin(GithubResponses.사용자1.getCode());

        final ExtractableResponse<Response> login = AuthStep.로그인(GithubResponses.사용자1.getEmail(), GithubResponses.사용자1.getCode());
        assertThat(login.jsonPath().getString("accessToken")).isNotBlank();
    }

    private static void assertLogin(String code) {
        final Map<String, String> params = new HashMap<>();
        params.put("code", code);

        final ExtractableResponse<Response> response = 깃허브_로그인(params);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
