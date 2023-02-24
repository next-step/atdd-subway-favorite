package nextstep.subway.unit;

import nextstep.member.application.AuthService;
import nextstep.member.application.GithubClient;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

public class AuthServiceMockTest extends MockTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private GithubClient githubClient;
    @Mock
    private MemberService memberService;

    private AuthService authService;

    private String code;
    private String accessToken;
    private GithubProfileResponse profile;
    private Member member;
    private String token;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    private GithubTokenRequest githubTokenRequest;

    @BeforeEach
    void setUp() {
        authService = new AuthService(jwtTokenProvider, memberService, githubClient);

        code = "code";
        accessToken = "accessToken";
        profile = new GithubProfileResponse("email@email.com");
        member = new Member(profile.getEmail());
        token = "token";

        githubTokenRequest = new GithubTokenRequest(code, clientId, clientSecret);
    }

    @DisplayName("깃허브 로그인으로 토큰을 생성한다.")
    @Test
    void createGithubToken() {
        given(githubClient.getAccessTokenFromGithub(code)).willReturn(accessToken);
        given(githubClient.getGithubProfileFromGithub(accessToken)).willReturn(profile);
        given(memberService.findByEmailOrCreate(profile.getEmail())).willReturn(member);
        given(jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles())).willReturn(token);

        final var response = authService.createToken(githubTokenRequest);

        assertThat(response.getAccessToken()).isNotBlank();
    }

    @DisplayName("유효하지 않은 권한증서로 깃허브 로그인을 요청하면 오류가 발생한다.")
    @Test
    void createGithubTokenWithInvalidCode() {
        given(githubClient.getAccessTokenFromGithub(code)).willThrow(InvalidTokenException.class);

        assertThatThrownBy(() -> authService.createToken(new GithubTokenRequest(code, null, null)))
                .isInstanceOf(InvalidTokenException.class);
    }
}
