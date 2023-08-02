package nextstep.auth.token;

import nextstep.auth.token.oauth2.OAuth2User;
import nextstep.auth.token.oauth2.OAuth2UserService;
import nextstep.auth.token.oauth2.github.GithubClient;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.member.domain.CustomOAuth2User;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("토큰 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TokenServiceTestWithMock {

    @InjectMocks
    private TokenService tokenService;
    @Mock
    private GithubClient githubClient;
    @Mock
    private OAuth2UserService oAuth2UserService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("깃 허브 토큰 생성 테스트 - Mocking")
    @Test
    void createTokenFromGithub() {
        // given : 선행조건 기술
        GithubProfileResponse githubProfile = createGithubProfileResponse();
        OAuth2User oAuth2User = createOAuth2User();
        String token = "token";
        given(githubClient.getAccessTokenFromGithub(GithubResponses.사용자1.getCode()))
                .willReturn(GithubResponses.사용자1.getAccessToken());
        given(githubClient.getGithubProfileFromGithub(GithubResponses.사용자1.getAccessToken()))
                .willReturn(githubProfile);
        given(oAuth2UserService.loadUser(githubProfile))
                .willReturn(oAuth2User);
        given(jwtTokenProvider.createToken(oAuth2User.getUsername(), oAuth2User.getRole()))
                .willReturn(token);

        // when : 기능 수행
        TokenResponse tokenResponse = tokenService.createTokenFromGithub(GithubResponses.사용자1.getCode());

        // then : 결과 확인
        assertThat(tokenResponse.getAccessToken()).isEqualTo(token);
        then(githubClient).should().getAccessTokenFromGithub(GithubResponses.사용자1.getCode());
        then(githubClient).should().getGithubProfileFromGithub(GithubResponses.사용자1.getAccessToken());
        then(oAuth2UserService).should().loadUser(githubProfile);
        then(jwtTokenProvider).should().createToken(oAuth2User.getUsername(), oAuth2User.getRole());
    }

    private GithubProfileResponse createGithubProfileResponse() {
        return new GithubProfileResponse(GithubResponses.사용자1.getEmail(), 20);
    }

    private CustomOAuth2User createOAuth2User() {
        return new CustomOAuth2User(GithubResponses.사용자1.getEmail(), "USER");
    }
}