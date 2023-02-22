package nextstep.subway.acceptance;

import nextstep.subway.utils.FakeGithubTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청하고_토큰_반환;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청하고_토큰_반환;
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

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        var token = 깃허브_인증_로그인_요청하고_토큰_반환(FakeGithubTokenResponse.사용자1.getCode());

        assertThat(token).isNotBlank();
    }
}