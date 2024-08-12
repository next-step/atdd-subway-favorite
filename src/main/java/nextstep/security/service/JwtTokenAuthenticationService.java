package nextstep.security.service;

import nextstep.security.payload.TokenResponse;
import nextstep.security.domain.Authentication;
import nextstep.security.domain.UserInfo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class JwtTokenAuthenticationService {

  private final UserDetailsService<Long, String> userDetailsService;
  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenAuthenticationService(final UserDetailsService<Long, String> userDetailsService, final JwtTokenProvider jwtTokenProvider) {
    this.userDetailsService = userDetailsService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public TokenResponse createToken(UserInfo userInfo) {
    Authentication<Long, String> authentication = userDetailsService.authenticateByUserInfo(userInfo);
    String token = jwtTokenProvider.createToken(authentication.getCredentials(), authentication.getPrincipal());
    return new TokenResponse(token);
  }

}
