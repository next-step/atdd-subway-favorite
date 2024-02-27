package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.GithubLoginRequest;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.FixtureUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

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
     * Given 멤버를 생성하고
     * When 멤버와 매칭되는 코드로 깃헙 로그인을 요청하면
     * Then 인증 토큰을 발급한다.
     */
    @DisplayName("기존 멤버의 깃헙 로그인")
    @Test
    void 기존_멤버의_깃헙_로그인() {
        // given
        MemberSteps.회원_생성_요청("domodazzi@gmail.com", "password", 20);
        var body = FixtureUtil.getBuilder(GithubLoginRequest.class)
            .set("code", "domodazzi")
            .sample();

        // when
        var response = 깃헙_로그인(body);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When 기존 멤버와 매칭되지 않는 코드로 깃헙 로그인을 요청하면
     * Then 회원가입을 진행 후 인증 토큰을 발급한다.
     */
    @DisplayName("회원이 아닌 멤버의 깃헙 로그인")
    @Test
    void 회원이_아닌_멤버의_깃헙_로그인() {
        // given
        var body = FixtureUtil.getBuilder(GithubLoginRequest.class)
            .set("code", "unknown")
            .sample();

        // when
        var response = 깃헙_로그인(body);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();

        var member = MemberSteps.회원_정보_조회_요청(response.jsonPath().getString("accessToken"))
            .as(MemberResponse.class);
        assertThat(member.getEmail()).isEqualTo("unknown@gmail.com");
    }

    private static ExtractableResponse<Response> 깃헙_로그인(GithubLoginRequest body) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/login/github")
            .then().log().all()
            .statusCode(HttpStatus.OK.value()).extract();
    }
}