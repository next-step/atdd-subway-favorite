package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.UserDetails;
import nextstep.auth.exception.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
  private final MemberService memberService;
  private final JwtTokenProvider jwtTokenProvider;
  private final GithubTokenClient githubTokenClient;
  private final GithubProfileClient githubProfileClient;
  private final UserDetailsService userDetailsService;

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
    GithubProfileResponse profile = githubProfileClient.getProfile(accessToken);

    Member member;
    try {
      member = memberService.findMemberByEmail(profile.getEmail());
    } catch (RuntimeException e) {
      throw new AuthenticationException();
    }

    String token = jwtTokenProvider.createToken(member.getEmail());
    return new TokenResponse(token);
  }
}
