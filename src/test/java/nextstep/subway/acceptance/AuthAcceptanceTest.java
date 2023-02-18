package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.*;

@DisplayName("인증 관리 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 10;

    @DisplayName("Bearer Auth에 성공한다.")
    @Test
    void bearerAuth() {

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        final ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        인증_로그인_응답_성공(response);
    }

    @DisplayName("패스워드를 잘못 입력해서 Bearer Auth 인증에 실패한다.")
    @Test
    void error_bearerAuth() {

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        final ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, "different password");

        인증_로그인_응답_실패(response);
    }

    @DisplayName("Github 로그인에 성공한다.")
    @Test
    void githubLogin() {

        final Map<String, String> params = createCode("832ovnq039hfjn");

        final ExtractableResponse<Response> github_로그인_응답 = github_인증_로그인_요청(params);

        github_인증_로그인_응답_성공(github_로그인_응답);
    }

    @DisplayName("Github 요청시 유효하지 않은 코드일 경우에는 예외처리한다.")
    @Test
    void error_githubLogin() {

        final Map<String, String> 존재하지_않는_코드 = createCode("code");

        final ExtractableResponse<Response> github_로그인_응답 = github_인증_로그인_요청(존재하지_않는_코드);

        github_인증_로그인_응답_실패(github_로그인_응답);
    }
}