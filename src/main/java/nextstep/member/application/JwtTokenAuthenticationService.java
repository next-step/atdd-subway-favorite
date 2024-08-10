package nextstep.member.application;

import javax.transaction.Transactional;
import nextstep.member.application.dto.TokenResponse;
import nextstep.security.domain.Authentication;
import nextstep.security.domain.UserInfo;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class JwtTokenAuthenticationService {

  private final MemberService memberService;
  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenAuthenticationService(JwtTokenProvider jwtTokenProvider,
      MemberService memberService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.memberService = memberService;
  }

  public TokenResponse createToken(UserInfo userInfo) {
    Authentication<Long, String> authentication = memberService.authenticateByUserInfo(userInfo);
    String token = jwtTokenProvider.createToken(authentication.getCredentials(), authentication.getPrincipal());
    return new TokenResponse(token);
  }

}
