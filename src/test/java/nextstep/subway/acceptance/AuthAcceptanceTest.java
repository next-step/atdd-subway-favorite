package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AuthSteps.깃헙_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String 이메일 = "email@email.com";
    private static final String 비밀번호 = "password";
    public static final int 나이 = 20;
    public static final GithubResponses 회원가입된_사용자 = GithubResponses.사용자1;
    public static final GithubResponses 회원가입이_안된_사용자 = GithubResponses.사용자2;
    /**
     * 회원을 생성하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_요청(이메일, 비밀번호, 나이);
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(이메일, 비밀번호);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("깃허브를 이용한 로그인 구현")
    @Test
    void githubAuth() {

        ExtractableResponse<Response> response = 깃헙_로그인_요청(회원가입된_사용자.getCode());

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("가입이 되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행")
    @Test
    void githubAuthWithNotMember() {

        ExtractableResponse<Response> response = 깃헙_로그인_요청(회원가입이_안된_사용자.getCode());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}