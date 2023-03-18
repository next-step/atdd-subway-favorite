package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.AuthService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.client.GithubClient;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.GithubResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("인증 관련 테스트")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private MemberService memberService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    private GithubClient githubClient;


    @BeforeEach
    void init() {
        githubClient = mock(GithubClient.class);
    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, 20);

        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * given : Github 계정이 있는 사용자가
     * when : Github을 통해 로그인하면
     * then : Github에서 해당 사용자 정보를 제공해준다.
     */
    @DisplayName("Github을 통해 로그인한다.")
    @Test
    void loginByGithub() {
        // given
        GithubResponse 사용자 = GithubResponse.사용자1;

        // when
        when(githubClient.getAccessTokenFromGithub(사용자.getCode())).thenReturn(사용자.getAccessToken());
        when(githubClient.getGithubProfileFromGithub(사용자.getAccessToken())).thenReturn(new GithubProfileResponse(사용자.getEmail()));
        AuthService authService = new AuthService(memberService, tokenProvider, githubClient);
        GithubAccessTokenResponse tokenResponse = authService.loginByGithub(사용자.getCode());

        // then
        GithubProfileResponse profileResponse = authService.getUserInfoFromGithub(tokenResponse.getAccessToken());
        assertThat(profileResponse.getEmail()).isEqualTo(사용자.getEmail());
    }

}