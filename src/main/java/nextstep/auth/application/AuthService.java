package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserDetailService userDetailService;
  private final JwtTokenProvider jwtTokenProvider;
  private final GithubClient githubClient;

  public TokenResponse createToken(String email, String password) {
    return null;
  }

  public TokenResponse createTokenFromGithub(final String code) {
    return null;
  }
}
