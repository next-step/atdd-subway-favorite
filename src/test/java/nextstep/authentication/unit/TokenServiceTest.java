package nextstep.authentication.unit;

import nextstep.authentication.application.AuthenticationService;
import nextstep.authentication.application.GithubClient;
import nextstep.authentication.application.JwtTokenProvider;
import nextstep.authentication.application.TokenService;
import nextstep.authentication.domain.AuthenticationInformation;
import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.authentication.application.dto.TokenResponse;
import nextstep.authentication.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.utils.UserInformation.사용자1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("토큰 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private GithubClient githubClient;

    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        tokenService = new TokenService(authenticationService, jwtTokenProvider, githubClient);
    }

    @DisplayName("토큰 생성 함수는, 사용자의 이메일과 비밀번호를 입력받아 토큰을 반환하다.")
    @Test
    void createTokenTest() {
        // given
        when(authenticationService.findMemberByEmail(사용자1.getEmail())).thenReturn(new AuthenticationInformation(사용자1.getEmail(), 사용자1.getId(), 사용자1.getPassword()));
        when(jwtTokenProvider.createToken(사용자1.getEmail(), 사용자1.getId())).thenReturn(사용자1.getAccessToken());

        // when
        TokenResponse accessToken = tokenService.createToken(사용자1.getEmail(), 사용자1.getPassword());

        // then
        assertThat(accessToken.getAccessToken()).isNotBlank();
    }

    @DisplayName("토큰 생성 함수는, 사용자의 코드를 입력받으면 토큰을 반환한다.")
    @Test
    void createTokenOfNotMemberTest() {
        // given
        when(githubClient.requestGithubToken(any())).thenReturn(사용자1.getAccessToken());
        when(githubClient.requestGithubProfile(any())).thenReturn(new GithubProfileResponse(사용자1.getEmail(), 사용자1.getAge()));
        when(authenticationService.lookUpOrCreateMember(any())).thenReturn(new LoginMember(사용자1.getEmail(), 사용자1.getId()));
        when(jwtTokenProvider.createToken(사용자1.getEmail(), 사용자1.getId())).thenReturn(사용자1.getAccessToken());

        // when
        TokenResponse accessToken = tokenService.createToken(사용자1.getCode());

        // then
        assertThat(accessToken.getAccessToken()).isNotBlank();
    }
}
