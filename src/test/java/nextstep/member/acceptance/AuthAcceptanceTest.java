package nextstep.member.acceptance;

import nextstep.common.acceptance.AcceptanceTest;
import nextstep.common.utils.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.member.acceptance.AuthSteps.토큰_발급_검증;
import static nextstep.member.acceptance.MemberSteps.*;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private DataLoader dataLoader;

    /**
     * When Email, Password로 로그인 요청하면
     * Then 토큰을 발급받는다.
     */
    @Test
    @DisplayName("로그인을 통한 토큰 발급")
    void bearerAuth() {
        // given
        dataLoader.loadData();

        // when
        var response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        // then
        토큰_발급_검증(response);
    }

    /**
     * When 깃허브 권한증서를 통해 로그인 요청하면
     * Then 토큰을 발급받는다.
     */
    @ParameterizedTest
    @ValueSource(strings = {"832ovnq039hfjn", "mkfo0aFa03m", "m-a3hnfnoew92"})
    @DisplayName("깃허브 권한증서를 통한 토큰 발급")
    void githubAuth(final String code) {
        // given
        dataLoader.loadData();

        // when
        var response = 깃허브_권한증서_로그인_요청(code);

        // then
        토큰_발급_검증(response);
    }

    /**
     * When 가입되지 않은 깃허브 로그인 요청하면
     * Then 회원 가입 진행 후 토큰을 발급받을 수 있고,
     * When 발급 받은 토큰으로 내 정보를 조회하면
     * Then 내 정보가 조회된다.
     */
    @Test
    @DisplayName("가입되지 않은 깃허브 로그인을 통한 토큰 발급")
    void unregisteredGithubLogin() {
        // when
        var loginResponse = 깃허브_권한증서_로그인_요청("nvci383mciq0oq");

        // then
        토큰_발급_검증(loginResponse);

        // when
        var myInfoResponse = 토큰으로_내_회원_정보_조회_요청(loginResponse.jsonPath().getString("accessToken"));

        // then
        소셜_로그인_회원_정보_조회(myInfoResponse, "email4@email.com");
    }
}
