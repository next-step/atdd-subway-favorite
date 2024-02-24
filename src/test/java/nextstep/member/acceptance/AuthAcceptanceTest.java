package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.request.GetAccessTokenRequest;
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

import static nextstep.member.acceptance.AuthAcceptanceStep.*;
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
        memberRepository.save(Member.of(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        String accessToken = 로그인_성공(EMAIL, PASSWORD);
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * When 등록된 코드로 토큰을 요청하면
     * Then 토큰을 발급 받는다.
     */
    @DisplayName("등록된 코드로 토큰을 요청하면 토큰을 발급 받는다.")
    @Test
    void 깃허브_로그인을_성공() {
        // then
        String 사용자1_코드 = GithubResponses.사용자_홍길동.getCode();

        // when
        String 토큰 = 깃허브_로그인_성공(사용자1_코드);

        // then
        깃허브_로그인_성공_검증(토큰, GithubResponses.사용자_홍길동);
    }

    /**
     * When 등록되지 않은 코드로 토큰을 요청하면
     * Then 토큰을 발급받지 못한다.
     */
    @DisplayName("등록되지 않은 코드로 토큰을 요청하면 토큰을 발급받지 못한다.")
    @Test
    void 깃허브_로그인을_실패() {
        // then
        String 미등록_사용자_코드 = "qwer1234";
        GetAccessTokenRequest 깃허브_로그인_요청 = GetAccessTokenRequest.from(미등록_사용자_코드);

        // when & then
        깃허브_로그인_실패_검증(깃허브_로그인_시도(깃허브_로그인_요청), HttpStatus.UNAUTHORIZED);
    }

    void 깃허브_로그인_성공_검증(String accessToken, GithubResponses githubResponses) {
        assertThat(accessToken).isNotBlank();
        assertThat(accessToken).isEqualTo(githubResponses.getAccessToken());
    }

    void 깃허브_로그인_실패_검증(ExtractableResponse<Response> extractableResponse, HttpStatus status) {
        assertThat(extractableResponse.statusCode()).isEqualTo(status.value());
    }


//    @DisplayName("Github Auth")
//    @Test
//    void githubAuth() {
//        // when
//        Map<String, String> params = new HashMap<>();
//        params.put("code", GithubResponses.사용자1.getCode());
//
//        ExtractableResponse<Response> response = RestAssured.given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(params)
//                .when().post("/login/github")
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        String token = response.jsonPath().getString(".");
//
//        // token을 이용해서 내 정보 조회를 했을 때 profileResponse
//        // assertThat(profileResponse.getEmail()).isEqualTo(GithubResponses.사용자1.getEmail());
//    }

}
