package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.CommonAcceptanceTest;
import nextstep.utils.GithubResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthAcceptanceTest extends CommonAcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    /** given 회원 정보를 생성하여 토큰을 발급받고
     *  when 발급된 토큰으로 회원 정보를 요청하면
     *  then 회원 정보를 응답 받는다
     */
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        //회원 정보 생성
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        //회원 정보(이메일)로 토큰 발급 (회원 정보가 없으면 오류 발생)
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        //발급된 토큰으로 회원 정보 찾기
        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        //토큰 발급 시 사용한 이메일과 응답 받은 회원 정보가 같은지 검증
        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * given 회원 정보를 생성하고
     * when 코드와 함께 깃헙 로그인 요청을 하면
     * then 액세스 토큰을 발급 받을 수 있다. (로그인 성공)
     */
    @Test
    void 깃허브_로그인_토큰발급() {
        //given
        memberRepository.save(new Member(GithubResponse.회원.getEmail(), PASSWORD, GithubResponse.회원.getAge()));

        //when
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponse.회원.getCode());

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/login/github")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        String accessToken = response.jsonPath().getString("accessToken");

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response2.jsonPath().getString("email")).isEqualTo(GithubResponse.회원.getEmail());
    }

    /**
     * when 깃허브 토큰이 없는 회원이 로그인 시도를 하면
     * then 인증오류가 발생한다.
     */
    @Test
    void 깃허브_토큰_없는_경우_로그인_인증오류() {
        //when
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponse.토큰_없음.getCode());

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/login/github")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}