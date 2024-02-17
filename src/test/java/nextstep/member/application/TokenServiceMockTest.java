package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.utils.ReflectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenServiceMockTest {

    @Mock
    private GithubClient githubClient;
    @Mock
    private MemberService memberService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("createTokenForGithub 를 통해 token 을 반환 받을 수 있다.")
    void createTokenForGithubTest() {
        final String code = "code";
        final String githubAccessToken = "github_access_token";
        final GithubProfileResponse githubProfileResponse = new GithubProfileResponse("test@test.com", 20);
        final Member member = createMemberFrom(githubProfileResponse);
        final String accessToken = "access_token";

        given(githubClient.requestGithubToken(code)).willReturn(githubAccessToken);
        given(githubClient.requestGithubProfile(githubAccessToken)).willReturn(githubProfileResponse);
        given(memberService.findOrCreateMember(githubProfileResponse)).willReturn(member);
        given(jwtTokenProvider.createToken(member.getId(), member.getEmail())).willReturn(accessToken);

        final TokenService tokenService = new TokenService(memberService, jwtTokenProvider, githubClient);
        final TokenResponse tokenResponse = tokenService.createTokenForGithub(code);

        assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);
    }

    private Member createMemberFrom(final GithubProfileResponse githubProfileResponse) {
        final Member member = new Member(githubProfileResponse.getEmail(), "", githubProfileResponse.getAge());
        ReflectionUtils.injectIdField(member, 1L);
        return member;
    }
}
