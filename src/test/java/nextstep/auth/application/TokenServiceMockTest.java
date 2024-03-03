package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceMockTest {
    @Mock
    private GithubClient githubClient;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;

    @DisplayName("github token 발급 테스트")
    @Test
    void createGithubTokenTest() {
        String code = "code";
        String githubAccessToken = "github_access_token";
        GithubProfileResponse githubProfileResponse = new GithubProfileResponse("test@test.com", 20);
        UserDetails userDetails = createUserDetailsFrom(githubProfileResponse);
        String accessToken = "access_token";

        when(githubClient.requestGithubToken(code)).thenReturn(githubAccessToken);
        when(githubClient.requestGithubProfile(githubAccessToken)).thenReturn(githubProfileResponse);
        when(userDetailsService.findOrCreateMember(githubProfileResponse.getEmail(), githubProfileResponse.getAge())).thenReturn(userDetails);
        when(jwtTokenProvider.createToken(githubProfileResponse.getEmail())).thenReturn(accessToken);

        TokenService tokenService = new TokenService(jwtTokenProvider, githubClient, userDetailsService);
        TokenResponse tokenResponse = tokenService.createGithubToken(code);

        assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);
    }

    private UserDetails createUserDetailsFrom(final GithubProfileResponse githubProfileResponse) {
        return new UserDetails(1L, githubProfileResponse.getEmail(), "", githubProfileResponse.getAge());
    }
}
