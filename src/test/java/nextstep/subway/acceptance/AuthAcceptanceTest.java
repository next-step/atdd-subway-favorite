package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.utils.AcceptanceTest;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.내_정보_조회;
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

        ExtractableResponse<Response> response = 토큰_발급_요청(params);

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response2 = 내_정보_조회(accessToken);

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    @DisplayName("기존 사용자 깃허브 Oauth 정상 로그인")
    @Test
    void 기존_사용자_깃허브_로그인() {
        //given
        memberRepository.save(new Member(GithubResponses.사용자2.getEmail(), "1234", 20));
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponses.사용자2.getCode());

        //when
        ExtractableResponse<Response> response = 깃허브_로그인_토큰_발급_요청(params);

        //then
        String subwayAccessToken = response.jsonPath().getString("accessToken");
        assertThat(subwayAccessToken).isNotBlank();

        ExtractableResponse<Response> 사용자_정보 = 내_정보_조회(subwayAccessToken);
        String 사용자_이메일 = 사용자_정보.jsonPath().getString("email");
        assertThat(사용자_이메일).isEqualTo(GithubResponses.사용자2.getEmail());
    }

    @DisplayName("비회원 사용자 깃허브 Oauth 정상 로그인")
    @Test
    void 비회원_사용자_깃허브_로그인() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponses.사용자4.getCode());

        //when
        ExtractableResponse<Response> response = 깃허브_로그인_토큰_발급_요청(params);

        //then
        String subwayAccessToken = response.jsonPath().getString("accessToken");
        assertThat(subwayAccessToken).isNotBlank();

        ExtractableResponse<Response> 사용자_정보 = 내_정보_조회(subwayAccessToken);
        String 사용자_이메일 = 사용자_정보.jsonPath().getString("email");
        assertThat(사용자_이메일).isEqualTo(GithubResponses.사용자4.getEmail());
    }

    private static ExtractableResponse<Response> 깃허브_로그인_토큰_발급_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    private static ExtractableResponse<Response> 토큰_발급_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }
}