package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.AuthSteps.소셜_로그인_요청;
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

    @Test
    void 깃허브_계정으로_로그인을_한다() {
        ExtractableResponse<Response> response = 소셜_로그인_요청("github", "code");

        액세스_토큰_검증(response);
    }

    private void 액세스_토큰_검증(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

}
