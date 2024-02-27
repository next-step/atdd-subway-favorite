package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserDetailService userDetailService;
  private final JwtTokenProvider jwtTokenProvider;
  private final GithubClient githubClient;

  public TokenResponse createToken(final String email, final String password) {
    final var userDetail = userDetailService.findByEmail(email)
        .orElseThrow(() -> new AuthenticationException("인증 정보가 올바르지 않습니다."));
    userDetail.checkPassword(password);

    final var token = jwtTokenProvider.createToken(userDetail.getEmail());

    return new TokenResponse(token);
  }

  public TokenResponse createTokenFromGithub(final String code) {
    final var accessToken = githubClient.requestGithubToken(code);
    final var profile = githubClient.requestGithubProfile(accessToken);

    final var userDetail = userDetailService.findByEmail(profile.getEmail())
        .orElseGet(() -> userDetailService.createUser(profile.getEmail(), "password", profile.getAge()));

    return createToken(userDetail.getEmail(), userDetail.getPassword());
  }
}
