package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.깃허브_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * When 로그인을 요청하면
     * Then 토큰을 발급 받는다
     */
    @DisplayName("Bearer Auth Token 발급")
    @Test
    void bearerAuth() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When 잘못된 Id나 Password 를 입력하면
     * Then 오류가 발생한다
     */
    @DisplayName("잘못된 유저 정보 입력")
    @Test
    void bearerAuth_WithInvalidUserInfo() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청("a", "b");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * When 회원이 Github 코드를 통해 로그인 요청을 하면
     * Then 토큰을 발급 받는다
     */
    @DisplayName("Github로 로그인 요청")
    @Test
    void githubAuth() {
        var response = 깃허브_로그인_요청(GithubResponses.사용자1.getCode());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("accessToken")).isNotBlank()
        );
    }
}
