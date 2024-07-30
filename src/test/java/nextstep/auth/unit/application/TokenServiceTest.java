package nextstep.auth.unit.application;

import static nextstep.Fixtures.aMember;
import static nextstep.auth.support.GithubResponses.사용자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import nextstep.auth.application.*;
import nextstep.auth.application.dto.OAuth2UserRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.exception.UsernameNotFoundException;
import nextstep.member.domain.DefaultUserDetails;
import nextstep.member.domain.GithubOAuth2User;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TokenService 단위 테스트")
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
  @Mock private JwtTokenProvider jwtTokenProvider;
  @Mock private GithubTokenClient githubTokenClient;
  @Mock private UserDetailsService userDetailsService;
  @Mock private OAuth2UserService oAuth2UserService;
  @InjectMocks private TokenService tokenService;

  @DisplayName("이메일과 비밀번호로 토큰을 생성한다.")
  @Test
  void createToken() {
    String accessToken = "xxxxxx.yyyyyy.zzzzzz";
    Member member = aMember().build();

    given(userDetailsService.loadUserByUsername(member.getEmail()))
        .willReturn(new DefaultUserDetails(member.getEmail(), member.getPassword()));
    given(jwtTokenProvider.createToken(member.getEmail())).willReturn(accessToken);

    TokenResponse tokenResponse = tokenService.createToken(member.getEmail(), member.getPassword());

    assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);
  }

  @DisplayName("이메일이 존재하지 않을 때 예외를 던진다.")
  @Test
  void createTokenEmailDoesNotExist() {
    String email = "does_not_exist@example.com";
    given(userDetailsService.loadUserByUsername(email))
        .willThrow(new UsernameNotFoundException(email));

    assertThatExceptionOfType(UsernameNotFoundException.class)
        .isThrownBy(() -> tokenService.createToken(email, "password"));
  }

  @DisplayName("깃헙 Oauth 인증 코드로 토큰을 생성한다.")
  @Test
  void createTokenFromGithubCode() {
    String code = 사용자.getCode();
    Member member = 사용자.getMember();
    String accessToken = "xxxxxx.yyyyyy.zzzzzz";

    given(githubTokenClient.getAccessToken(code)).willReturn(사용자.getAccessToken());
    given(oAuth2UserService.loadUser(any(OAuth2UserRequest.class)))
        .willReturn(new GithubOAuth2User(member.getEmail()));
    given(jwtTokenProvider.createToken(member.getEmail())).willReturn(accessToken);

    TokenResponse tokenResponse = tokenService.createTokenFromGithubCode(code);

    assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);
  }

  @DisplayName("깃헙 프로필 사용자가 회원 가입되어 있지 않을 때 예외를 던진다.")
  @Test
  void emailFromGithubDoesNotExist() {
    given(githubTokenClient.getAccessToken("code")).willReturn("gh_access_token");
    given(oAuth2UserService.loadUser(any(OAuth2UserRequest.class)))
        .willThrow(new AuthenticationException());

    assertThatExceptionOfType(AuthenticationException.class)
        .isThrownBy(() -> tokenService.createTokenFromGithubCode("code"));
  }
}
