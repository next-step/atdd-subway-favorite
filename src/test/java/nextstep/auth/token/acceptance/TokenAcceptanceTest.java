package nextstep.auth.token.acceptance;


import static nextstep.auth.token.acceptance.TokenSteps.로그인_요청;
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
    void token() {
        /* TODO: 만약 패키지가 분리되어 있다면 테스트 작성할 때도 패키지를 분리해야 할까요?
        회원_생성_요청(MemberSteps)이 다른 패키지에 존재하고 있는데,
        이렇게 되면 패키지 별로 분리했을 때 테스트가 실패하게 됩니다. 그렇다면 코드 중복을 감수하더라도 이 패키지에 테스트를 작성해야 할까요?
         */
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        var 토큰 = 로그인_요청(EMAIL, PASSWORD);

        Assertions.assertThat(jwtTokenProvider.validateToken(토큰)).isTrue();
    }
}