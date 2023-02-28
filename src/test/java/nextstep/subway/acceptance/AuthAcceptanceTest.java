package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import nextstep.member.application.dto.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LoginSteps.깃헙_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.토큰으로_내_정보_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        super.setUp();
        dataLoader.loadData();
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * Given 회원 가입
     * When Github 로그인 요청 시
     * Then accessToken 을 얻을 수 있다.
     */
    @DisplayName("Github 로그인 요청 시 accessToken 을 얻을 수 있다")
    @Test
    void Github_로그인_요청_시_accessToken_을_얻을_수_있다() {
        // Given
        dataLoader.loadDataWithGithubUser();

        // When
        ExtractableResponse<Response> response = 깃헙_로그인_요청(GithubResponses.사용자1.getCode());

        // Then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When 가입이 되어있지 않은 회원이 로그인 요청 시
     * Then 가입 진행 후
     * Then Access Token 을 확인 할 수 있다
     */
    @DisplayName("가입이 되어있지 않은 회원이 로그인 요청 시 가입 진행 후 Access Token 을 확인 할 수 있다")
    @Test
    void 가입이_되어있지_않은_회원이_로그인_요청_시_가입_진행_후_Access_Token_을_확인_할_수_있다_() {
        // When
        String accessToken = 깃헙_로그인_요청(GithubResponses.사용자1.getCode()).jsonPath().getString("accessToken");

        // Then
        ExtractableResponse<Response> response = 토큰으로_내_정보_요청(accessToken);

        assertThat(accessToken).isNotBlank();
        assertThat(response.jsonPath().getString("email")).isEqualTo(GithubResponses.사용자1.getEmail());
    }

}
