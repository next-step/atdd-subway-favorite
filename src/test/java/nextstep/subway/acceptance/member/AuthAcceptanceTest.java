package nextstep.subway.acceptance.member;

import nextstep.fake.DataLoader;
import nextstep.fake.GithubResponses;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.member.MemberSteps.깃허브_권한증서로_로그인_요청;
import static nextstep.subway.acceptance.member.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.member.MemberSteps.토큰_인증으로_내_회원_정보_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

 @DisplayName("인증 관련 기능")
class AuthAcceptanceTest extends AcceptanceTest {
     @Autowired
     private DataLoader dataLoader;

     private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        dataLoader.loadData();
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

    /**
     * When 미리 정의된 사용자의 code 로 토큰 발급을 요청
     * Then 토큰을 발급 받는다
     */
    @DisplayName("Github 권한증서 인증")
    @Test
    void githubAuth() {
        var response = 깃허브_권한증서로_로그인_요청(GithubResponses.관리자.getCode());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        });
    }

    /**
     * Given 깃허브 인증을 하고
     * When 인증된 정보로 내 정보를 요청하면
     * Then 내 정보를 확인할 수 있다.
     */
    @DisplayName("깃허브 로그인 후 내 정보 확인")
    @Test
    void checkMyProfile() {
        var loginResponse = 깃허브_권한증서로_로그인_요청(GithubResponses.관리자.getCode());
        String token = loginResponse.jsonPath().getString("accessToken");

        var response = 토큰_인증으로_내_회원_정보_조회_요청(token);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getString("email")).isEqualTo(GithubResponses.관리자.getEmail());
            assertThat(response.jsonPath().getInt("age")).isEqualTo(20);
        });
    }

    /**
     * Given 가입이 되어있지 않은 회원이
     * When 깃허브 로그인을 하면
     * Then 가입된 정보를 확인할 수 있다.
     */
    @DisplayName("미가입 회원 깃허브 로그인 후 내 정보 확인")
    @Test
    void registerAndcheckMyProfile() {
        var loginResponse = 깃허브_권한증서로_로그인_요청(GithubResponses.사용자4.getCode());
        String token = loginResponse.jsonPath().getString("accessToken");

        var response = 토큰_인증으로_내_회원_정보_조회_요청(token);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getString("email")).isEqualTo(GithubResponses.사용자4.getEmail());
            assertThat(response.jsonPath().getString("age")).isNull();
        });
    }

    /**
     * When 잘못된 Github code 로 토큰 발급을 요청
     * Then 토큰을 발급 받을 수 없다.
     */
    @DisplayName("잘못된 Github 권한증서 입력")
    @Test
    void githubAuthException() {
        var response = 깃허브_권한증서로_로그인_요청("wrong code");

        assertAll(() -> {
            assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getString("accessToken")).isNull();
        });
    }
}
