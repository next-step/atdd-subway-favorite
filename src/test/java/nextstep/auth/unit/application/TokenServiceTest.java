package nextstep.auth.unit.application;

import static nextstep.Fixtures.aMember;
import static nextstep.auth.support.GithubResponses.사용자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

import nextstep.auth.application.*;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.exception.UsernameNotFoundException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.CustomUserDetails;
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
  @Mock private MemberService memberService;
  @Mock private JwtTokenProvider jwtTokenProvider;
  @Mock private GithubTokenClient githubTokenClient;
  @Mock private GithubProfileClient githubProfileClient;
  @Mock private UserDetailsService userDetailsService;
  @InjectMocks private TokenService tokenService;

  @DisplayName("이메일과 비밀번호로 토큰을 생성한다.")
  @Test
  void createToken() {
    String accessToken = "xxxxxx.yyyyyy.zzzzzz";
    Member member = aMember().build();
    given(userDetailsService.loadUserByUsername(member.getEmail()))
        .willReturn(new CustomUserDetails(member.getEmail(), member.getPassword()));
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
    given(githubProfileClient.getProfile(사용자.getAccessToken()))
        .willReturn(new GithubProfileResponse("", "", member.getEmail()));
    given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);
    given(jwtTokenProvider.createToken(member.getEmail())).willReturn(accessToken);

    TokenResponse tokenResponse = tokenService.createTokenFromGithubCode(code);

    assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);
  }

  @DisplayName("깃헙 프로필 사용자가 회원 가입되어 있지 않을 때 예외를 던진다.")
  @Test
  void emailFromGithubDoesNotExist() {
    String code = "code";
    String accessToken = "gho_16C7e42F292c6912E7710c838347Ae178B4a";
    String email = "does_not_exist@example.com";
    given(githubTokenClient.getAccessToken(code)).willReturn(accessToken);
    given(githubProfileClient.getProfile(accessToken))
        .willReturn(new GithubProfileResponse("", "", email));
    given(memberService.findMemberByEmail(email)).willThrow(new RuntimeException());

    assertThatExceptionOfType(AuthenticationException.class)
        .isThrownBy(() -> tokenService.createTokenFromGithubCode(code));
  }
}
