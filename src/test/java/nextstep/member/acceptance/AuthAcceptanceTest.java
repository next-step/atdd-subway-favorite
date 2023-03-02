package nextstep.member.acceptance;

import static nextstep.member.acceptance.AuthAcceptanceSteps.Github_로그인_검증;
import static nextstep.member.acceptance.AuthAcceptanceSteps.Github_로그인_요청;
import static nextstep.member.acceptance.AuthAcceptanceSteps.Github에_가입이_되어있지_않은_경우_예외_처리_검증;
import static nextstep.member.acceptance.AuthAcceptanceSteps.베어러_인증_로그인_요청;
import static nextstep.member.acceptance.AuthAcceptanceSteps.베어러_인증_로그인에_실패하면_예외_처리한다;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import java.util.UUID;
import nextstep.utils.AcceptanceTest;
import nextstep.utils.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private DataLoader dataLoader;

    @Override
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
     * when 베어러 인증 로그인에 실패하면
     * then 예외 처리한다.
     */
    @DisplayName("베어러 인증 로그인에 실패하면 예외 처리한다.")
    @Test
    void bearerAuthLoginFail() {
        베어러_인증_로그인에_실패하면_예외_처리한다(EMAIL, PASSWORD + UUID.randomUUID());
    }

    /**
     * given Github에서 발급받은 권한증서로
     * when 로그인을 요청하면
     * then accessToken을 발급한다.
     */
    @DisplayName("Github에서 발급받은 권한증서로 로그인을 요청하면 accessToken을 발급한다.")
    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"832ovnq039hfjn", "mkfo0aFa03m", "a3hnfnoew92", "nvci383mciq0oq"})
    void githubAuth(final String code) {
        Map<String, String> params = Map.of("code", code);

        var response = Github_로그인_요청(params);

        Github_로그인_검증(response);
    }

    /**
     * given Github에서 발급받지 않은 권한증서로
     * when 로그인을 요청하면
     * then 예외 처리한다.
     */
    @DisplayName("Github에 가입이 되어있지 않은 경우 회원 가입을 먼저 진행해야 한다.")
    @Test
    void githubUnAuthorized() {
        Map<String, String> params = Map.of("code", "code");

        var response = Github_로그인_요청(params);

        Github에_가입이_되어있지_않은_경우_예외_처리_검증(response);
    }
}
