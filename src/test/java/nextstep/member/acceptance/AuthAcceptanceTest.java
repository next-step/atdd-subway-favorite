package nextstep.member.acceptance;

import nextstep.DataLoader;
import nextstep.common.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.member.acceptance.AuthSteps.토큰_발급_검증;
import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청;

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
}