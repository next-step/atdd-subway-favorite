package nextstep.auth.acceptance;

import static nextstep.auth.steps.TokenSteps.깃허브_토근_생성_요청;
import static nextstep.auth.steps.TokenSteps.토큰_생성_응답에서_토큰값_추출;
import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.auth.fixture.GithubResponses;
import nextstep.auth.fixture.TokenFixture;
import nextstep.member.acceptance.steps.MemberSteps;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.context.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@AcceptanceTest
class AuthAcceptanceTest {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
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
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();

        // then
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
     * given 사용자가 존재하고
     * when github 로그인을 시도하면
     * then subway accessToken 값을 조회할 수 있다.
     */
    @DisplayName("깃허브 토큰 생성 ")
    @Test
    void githubAuth() {
        GithubResponses 회원 = GithubResponses.사용자1;
        MemberSteps.회원_생성_요청(회원.getEmail(), 회원.getEmail(), 10);

        //when
        ExtractableResponse<Response> 깃헙_로그인_요청_응답 =
            깃허브_토근_생성_요청(TokenFixture.깃허브_토근_생성_요청_본문(회원.getCode()));

        //then
        assertThat(토큰_생성_응답에서_토큰값_추출(깃헙_로그인_요청_응답)).isNotBlank();
    }
}
