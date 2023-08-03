package nextstep.auth.acceptance;

import nextstep.support.AcceptanceTest;
import nextstep.support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static nextstep.auth.acceptance.step.AuthStep.깃허브_로그인_요청;
import static nextstep.auth.acceptance.step.AuthStep.일반_로그인_요청;
import static nextstep.auth.acceptance.utils.GithubMockUser.김민지;
import static nextstep.member.acceptance.step.MemberStep.회원_생성_요청;
import static nextstep.member.fixture.MemberFixture.회원_정보_DTO;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanUp;

    private static final String ACCESS_TOKEN_KEY = "accessToken";

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();

        회원_생성_요청(회원_정보_DTO);
    }

    /**
     * When 일반 로그인을 하면
     * Then 토큰을 발급 받는다
     */
    @DisplayName("일반 로그인을 하면 토큰을 발급 받는다")
    @Test
    void bearerAuth() {
        // when
        var 일반_로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), 회원_정보_DTO.getPassword());

        // then
        assertThat(일반_로그인_응답.jsonPath().getString(ACCESS_TOKEN_KEY)).isNotBlank();
    }

    /**
     * When 깃허브 로그인을 하면
     * Then 토큰을 발급 받는다
     */
    @DisplayName("깃허브 로그인을 하면 토큰을 발급 받는다")
    @Test
    void githubAuth() {
        // when
        var 깃허브_토큰_응답 = 깃허브_로그인_요청(김민지.getCode());

        // then
        assertThat(깃허브_토큰_응답.jsonPath().getString(ACCESS_TOKEN_KEY)).isNotBlank();
    }
}