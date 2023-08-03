package nextstep.auth.token.acceptance;


import static nextstep.auth.token.acceptance.TokenSteps.로그인_요청;
import static nextstep.auth.token.acceptance.TokenSteps.토큰_검증_통과;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;

import nextstep.auth.token.JwtTokenProvider;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TokenAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@gmail.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    TokenAcceptanceTest(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Given 회원 가입 신청하고
     * When 가인한 회원으로 로그인 신청하면
     * Then 토큰을 반환받는다
     */
    @DisplayName("로그인을 요청하면 토큰을 반환한다")
    @Test
    void tokenTest() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        var 토큰 = 로그인_요청(EMAIL, PASSWORD);

        토큰_검증_통과(jwtTokenProvider, 토큰);
    }


}