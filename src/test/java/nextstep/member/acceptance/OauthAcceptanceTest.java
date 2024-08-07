package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.acceptance.test.GithubUser;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.member.acceptance.MemberAssuredTemplate.자기자신정보요청;
import static nextstep.member.acceptance.OauthAssuredTemplate.깃허브로그인;

public class OauthAcceptanceTest extends AcceptanceTest {

    /**
     * Given github 인증을 진행합니다.
     * When 기존에 한 번 가입한 회원이 아닌경우 새로 가입 후 토큰을 발급받습니다.
     * Then 발급받은 토큰으로 사용자를 찾으면 해당 사용자를 응답받습니다.
     */
    @DisplayName("기존에 가입된 유저가 아닌 경우 회원가입 페이지로 이동하라는 응답을 전달합니다.")
    @Test
    void registerNewUser() {
        // given
        ExtractableResponse<Response> loginResult = 깃허브로그인(GithubUser.사용자1.getCode())
                .then().extract();

        // when
        Assertions.assertThat(loginResult.statusCode()).isEqualTo(HttpStatus.OK.value());

        String accessToken = loginResult.jsonPath().getString("accessToken");
        Assertions.assertThat(accessToken).isNotBlank();

        // then
        ExtractableResponse<Response> memberResult = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when()
                .get("/members/me")
                .then().extract();

        Assertions.assertThat(memberResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(memberResult.jsonPath().getString("email")).isEqualTo(GithubUser.사용자1.getEmail());
        Assertions.assertThat(memberResult.jsonPath().getInt("age")).isEqualTo(GithubUser.사용자1.getAge());
    }

    /**
     * Given 이미 기존에 github 인증을 통해 가입한 상태입니다.
     * When 새로 github 인증을 진행합니다.
     * Then 정상적인 토큰을 전달받습니다.
     */
    @DisplayName("이미 가입한 유저의 경우 토큰을 생성한 후 응답합니다.")
    @Test
    void existUser() {
        // given
        String accessToken = 깃허브로그인(GithubUser.사용자1.getCode())
                .then().extract().jsonPath().getString("accessToken");

        ExtractableResponse<Response> memberResult = 자기자신정보요청(accessToken)
                .then().extract();

        Assertions.assertThat(memberResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(memberResult.jsonPath().getString("email")).isEqualTo(GithubUser.사용자1.getEmail());
        Assertions.assertThat(memberResult.jsonPath().getInt("age")).isEqualTo(GithubUser.사용자1.getAge());

        // when
        ExtractableResponse<Response> loginResult = 깃허브로그인(GithubUser.사용자1.getCode())
                .then().extract();
        // then
        Assertions.assertThat(loginResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(loginResult.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * Given github 인증을 진행합니다.
     * When 올바르지 않은 인증입니다.
     * Then 권한이 없다는 에러 응답을 전달합니다.
     */
    @DisplayName("인증 코드가 잘못된 경우 에러 응답을 반환합니다.")
    @Test
    void invalidCode() {
        // given
        String code = "asdfajsdkfjskldjkflj";
        // when
        ExtractableResponse<Response> result = 깃허브로그인(code)
                .then().extract();
        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
