package nextstep.subway.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.application.provider.TokenType;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.testhelper.AcceptanceTest;
import nextstep.subway.testhelper.apicaller.TokenApiCaller;
import nextstep.subway.testhelper.fixture.GithubResponsesFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.testhelper.apicaller.MemberApiCaller.내_정보_조회_요청;
import static nextstep.subway.testhelper.apicaller.MemberApiCaller.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;

class TokenAcceptanceTest extends AcceptanceTest {
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
     * Given 이미 가입한 회원이
     * When 로그인에 필요한 코드를 입력하면
     * Then 로그인 토큰이 발행된다.
     */
    @DisplayName("로그인에 필요한 코드를 입력하면 로그인 토큰이 발행된다.")
    @Test
    void githubAuth() {
        // Given
        memberRepository.save(new Member(GithubResponsesFixture.사용자1.getEmail(), PASSWORD,
                GithubResponsesFixture.사용자1.getAge()));

        // When
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponsesFixture.사용자1.getCode());

        // then
        String actual = TokenApiCaller.깃허브_로그인(params).jsonPath().getString("accessToken");
        String expected = GithubResponsesFixture.사용자1.getAccessToken();
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * given 로그인에 필요한 코드를 입력하면
     * When 로그인 토큰이 발행되고 정보를 조회 시 없다면
     * Then 회원가입이 진행 후 토큰이 발급된다.
     */
    @DisplayName("로그인에 필요한 코드를 입력하면 로그인 토큰이 발행된다.")
    @Test
    void githubAuthNotMember() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponsesFixture.사용자1.getCode());

        // when
        boolean actual = memberRepository.findByEmail(GithubResponsesFixture.사용자1.getEmail()).isEmpty();
        boolean expected = true;
        assertThat(actual).isEqualTo(expected);

        // then
        String actualToken = TokenApiCaller.깃허브_로그인(params).jsonPath().getString("accessToken");

        var response = 내_정보_조회_요청(actualToken, TokenType.GITHUB);
        회원_정보_조회됨(response, GithubResponsesFixture.사용자1.getEmail(), 0);
    }
}
