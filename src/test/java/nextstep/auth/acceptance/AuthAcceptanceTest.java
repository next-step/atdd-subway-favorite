package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.auth.acceptance.AuthSteps.소셜_로그인_요청;
import static nextstep.auth.application.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
        ExtractableResponse<Response> response = AuthSteps.로그인_요청(EMAIL, PASSWORD);

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
     * When Github 로그인 요청시 올바르지 않은 code값일 경우
     * Then 로그인을 할 수 없어 토큰 발급에 실패한다.
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "asd"})
    void 실패_깃허브_계정으로_로그인시_올바르지_않은_code값일_경우_로그인을_할_수_없다(String code) {
        ExtractableResponse<Response> response = 소셜_로그인_요청(UNAUTHORIZED.value(), "github", code);

        토큰_발급_실패(response);
    }

    private void 토큰_발급_실패(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("message")).isEqualTo("인증에 실패했습니다.");
    }

    /**
     * When Github 로그인 요청시
     * Then 로그인을 할 수 있어 토큰값을 발급 받는다.
     */
    @Test
    void 성공_깃허브_계정으로_로그인을_성공한다() {
        ExtractableResponse<Response> response = 소셜_로그인_요청(OK.value(), "github", 사용자1.getCode());

        액세스_토큰_검증(response);
    }

    private void 액세스_토큰_검증(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

}
