package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.AuthSteps.Access_Token_조회됨;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.controller.GithubResponses.회원가입_한_사용자1;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    /**
     * GIVEN 회원가입을 한 사용자가 있다 <br>
     * WHEN 사용자의 이메일과 비밀번호로 로그인을 하는 경우 <br>
     * THEN 액세스 토큰을 가져온다 <br>
     */
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        Access_Token_조회됨(베어러_인증_로그인_응답);
    }

    /**
     * GIVEN 깃허브 인증으로 회원가입을 한 사용자가 있다 <br>
     * WHEN 사용자가 깃허브 인증 로그인을 하는 경우 <br>
     * THEN 액세스 토큰을 가져온다 <br>
     */
    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        var 깃허브_인증_로그인_응답 = 깃허브_인증_로그인_요청(회원가입_한_사용자1.getCode());

        Access_Token_조회됨(깃허브_인증_로그인_응답);
    }
}
