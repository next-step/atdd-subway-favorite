package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.AcceptanceUtils.응답코드_400을_반환한다;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청하고_토큰_반환;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청하고_토큰_반환;
import static nextstep.subway.utils.FakeGithubTokenResponse.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        var token = 베어러_인증_로그인_요청하고_토큰_반환(EMAIL, PASSWORD);

        assertThat(token).isNotBlank();
    }

    /**
     * given: 깃허브에서 받은 권한증서를 이용하여
     * when : 로그인 요청을 하면
     * then : 액세스 토큰을 발급 받는다
     */
    @DisplayName("깃허브 권한증서로 로그인 요청을하면 액세스 토큰을 발급받는다.")
    @Test
    void githubAuth() {
        // given
        final String code = 사용자1.getCode();

        // when
        var token = 깃허브_인증_로그인_요청하고_토큰_반환(code);

        // then
        assertThat(token).isNotBlank();
    }


    /**
     * given: 유효하지 않은 권한증서를 이용하여
     * when : 로그인 요청을 하면
     * then : 오류가 발생한다.
     */
    @DisplayName("유효하지 않은 권한증서로 로그인 요청을하면 오류가 발생한다.")
    @Test
    void githubAuthInvalidCode() {
        // given
        final var code = "invalid code";

        // when
        final var response = 깃허브_인증_로그인_요청(code);

        // then
        응답코드_400을_반환한다(response);
    }
}