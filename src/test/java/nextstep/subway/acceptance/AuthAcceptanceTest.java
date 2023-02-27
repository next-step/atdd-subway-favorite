package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.AuthSteps.ACCESS_TOKEN_이_생성됨;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        ACCESS_TOKEN_이_생성됨(베어러_인증_로그인_응답);
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        var githubCode = "832ovnq039hfjn";
        var 깃허브_인증_로그인_응답 = 깃허브_인증_로그인_요청(githubCode);

        ACCESS_TOKEN_이_생성됨(깃허브_인증_로그인_응답);
    }
}
