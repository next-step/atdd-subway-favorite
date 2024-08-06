package nextstep.member.acceptance;

import static nextstep.member.acceptance.AuthSteps.responseToAccessToken;
import static nextstep.member.acceptance.AuthSteps.로그인;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.member.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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

        ExtractableResponse<Response> response = 로그인(EMAIL, PASSWORD);

        String 로그인_토큰 = responseToAccessToken(response);
        assertThat(로그인_토큰).isNotBlank();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(로그인_토큰)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }


    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponses.사용자1.getCode());

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + GithubResponses.사용자1.getAccessToken())
            .body(params)
            .when().post("/login/github")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();

        String accessToken = response.as(TokenResponse.class).getAccessToken();
        assertThat(accessToken).isNotBlank();
        assertThat(accessToken).isEqualTo(GithubResponses.사용자1.getAccessToken());
    }
}