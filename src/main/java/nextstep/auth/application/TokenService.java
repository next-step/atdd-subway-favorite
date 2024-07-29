package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.dto.OAuth2UserRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.OAuth2User;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
  private final JwtTokenProvider jwtTokenProvider;
  private final GithubTokenClient githubTokenClient;
  private final UserDetailsService userDetailsService;
  private final OAuth2UserService oAuth2UserService;

  public TokenResponse createToken(String email, String password) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    if (!userDetails.getPassword().equals(password)) {
      throw new AuthenticationException();
    }

    String token = jwtTokenProvider.createToken(userDetails.getUsername());
    return new TokenResponse(token);
  }

  public TokenResponse createTokenFromGithubCode(String code) {
    String accessToken = githubTokenClient.getAccessToken(code);

    OAuth2User oAuth2User = oAuth2UserService.loadUser(new OAuth2UserRequest(accessToken));

    String token = jwtTokenProvider.createToken(oAuth2User.getName());
    return new TokenResponse(token);
  }
}
