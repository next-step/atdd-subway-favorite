package nextstep.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.AcceptanceTest;
import nextstep.auth.utils.fixture.GithubAuthFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static nextstep.auth.utils.steps.AuthSteps.*;
import static nextstep.member.utils.steps.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    /**
     * Scenario: 신규 회원에 대한 토큰 생성
     * Given 신규 회원 생성
     * When 생성한 회원의 토큰 생성 요청
     * Then 생성한 토큰으로 내 정보를 조회할 수 있다.
     */
    @DisplayName("토큰 생성 요청")
    @Test
    void bearerAuth() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 토큰_생성_요청(EMAIL, PASSWORD);

        // then
        생성한_토큰으로_내_정보_조회_성공(response, EMAIL);
    }

    /**
     * Scenario: 이미 가입된 회원으로 깃헙 로그인 성공
     * Given 회원가입으로 회원을 생성
     * When 깃헙 로그인 요청 시
     * Then accessToken이 올바르게 생성된다.
     */
    @DisplayName("가입된 회원 깃헙 로그인 요청")
    @Test
    void 가입된_회원_깃헙_로그인() {
        // given
        GithubAuthFixture 회원 = GithubAuthFixture.사용자1;
        회원_생성_요청(회원.getEmail(), 회원.getPassword(), 10);

        // when
        ExtractableResponse<Response> response = 깃헙_정보로_토큰_생성_요청(회원.getCode());

        // then
        생성한_토큰으로_내_정보_조회_성공(response, 회원.getEmail());
    }

    /**
     * Scenario: 가입 안된 회원으로 깃헙 로그인 성공
     * Given 존재하지 않는 회원으로
     * When 깃헙 로그인 요청 시
     * Then 생성된 accessToken으로 내 정보를 조회할 수 있다.
     */
    @DisplayName("미가입 회원 깃헙 로그인 요청")
    @Test
    void 미가입_회원_깃헙_로그인() {
        // given
        GithubAuthFixture 회원 = GithubAuthFixture.사용자2;
        var createResponse = 회원_생성_요청(회원.getEmail(), 회원.getPassword(), 10);
        회원_삭제_요청(createResponse);

        // when
        ExtractableResponse<Response> response = 깃헙_정보로_토큰_생성_요청(회원.getCode());

        // then
        생성한_토큰으로_내_정보_조회_성공(response, 회원.getEmail());
    }

    private void 생성한_토큰으로_내_정보_조회_성공(ExtractableResponse<Response> response, String email) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        assertThat(내_정보_조회(accessToken).jsonPath().getString("email")).isEqualTo(email);
    }
}
