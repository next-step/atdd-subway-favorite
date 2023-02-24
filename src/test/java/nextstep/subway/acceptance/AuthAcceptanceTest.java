package nextstep.subway.acceptance;

import nextstep.subway.utils.FakeGithubTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nextstep.subway.acceptance.AcceptanceUtils.응답코드_400을_반환한다;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청하고_토큰_반환;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청하고_토큰_반환;
import static nextstep.subway.acceptance.MemberSteps.토근_인증으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;
import static nextstep.subway.utils.FakeGithubTokenResponse.사용자1;
import static nextstep.subway.utils.FakeGithubTokenResponse.사용자2;
import static nextstep.subway.utils.FakeGithubTokenResponse.사용자3;
import static nextstep.subway.utils.FakeGithubTokenResponse.사용자4;
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
    @MethodSource("codeProvider")
    @ParameterizedTest(name = "{0}")
    void githubAuth(FakeGithubTokenResponse tokenResponse) {
        // given
        final String code = tokenResponse.getCode();

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


    /**
     * given: 깃허브 로그인 요청을 통해 얻은 액세스 토큰으로
     * when : 회원 정보 조회 요청을 하면
     * then : 회원 정보를 반환한다.
     */
    @DisplayName("깃허브 로그인 요청으로 얻은 토큰으로 회원 정보를 조회할 수 있다.")
    @MethodSource("codeProvider")
    @ParameterizedTest(name = "{0}")
    void githubAuthAndGetInfo(FakeGithubTokenResponse tokenResponse) {
        // given
        var token = 깃허브_인증_로그인_요청하고_토큰_반환(tokenResponse.getCode());

        // when
        final var response = 토근_인증으로_내_회원_정보_조회_요청(token);

        // then
        회원_정보_조회됨(response, tokenResponse.getEmail());
    }

    private static Stream<Arguments> codeProvider() {
        return Stream.of(
                Arguments.of(사용자1),
                Arguments.of(사용자2),
                Arguments.of(사용자3),
                Arguments.of(사용자4)
        );
    }
}