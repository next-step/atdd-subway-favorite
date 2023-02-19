package nextstep.subway.acceptance.member;

import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.member.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Override
    @BeforeEach
    public void setUp() {
    }

    /**
     * When 로그인을 요청하면
     * Then 토큰을 발급 받는다
     */
    @DisplayName("Bearer Auth Token 발급")
    @Test
    void bearerAuth() {
        String token = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(token).isNotBlank();
    }

    /**
     * When 올바르지 않은 아이디/패스워드를 입력하면
     * Then 토큰을 받을 수 없다.
     */
    @DisplayName("잘못된 인증 정보 입력")
    @Test
    void bearerAuthException() {
        String token = 베어러_인증_로그인_요청("wrongEmail@email.com", "wrongPassword");

        assertThat(token).isNull();
    }
}
