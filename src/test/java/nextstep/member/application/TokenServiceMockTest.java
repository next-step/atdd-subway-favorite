package nextstep.member.application;

import nextstep.favorite.application.GithubClient;
import nextstep.favorite.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
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
    private MemberService memberService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("github token 발급 테스트")
    @Test
    void createGithubTokenTest() {
        String code = "code";
        String githubAccessToken = "github_access_token";
        GithubProfileResponse githubProfileResponse = new GithubProfileResponse("test@test.com", 20);
        Member member = createMemberFrom(githubProfileResponse);
        String accessToken = "access_token";

        when(githubClient.requestGithubToken(code)).thenReturn(githubAccessToken);
        when(githubClient.requestGithubProfile(githubAccessToken)).thenReturn(githubProfileResponse);
        when(memberService.findOrCreateMember(githubProfileResponse)).thenReturn(member);
        when(jwtTokenProvider.createToken(member.getEmail())).thenReturn(accessToken);

        TokenService tokenService = new TokenService(memberService, jwtTokenProvider, githubClient);
        TokenResponse tokenResponse = tokenService.createGithubToken(code);

        assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);
    }

    private Member createMemberFrom(final GithubProfileResponse githubProfileResponse) {
        final Member member = new Member(1L, githubProfileResponse.getEmail(), "", githubProfileResponse.getAge());
        return member;
    }
}
