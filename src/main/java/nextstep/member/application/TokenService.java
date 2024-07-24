package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
  private final MemberService memberService;
  private final JwtTokenProvider jwtTokenProvider;

  public TokenResponse createToken(String email, String password) {
    Member member;
    try {
      member = memberService.findMemberByEmail(email);
    } catch (RuntimeException e) {
      throw new AuthenticationException();
    }

    if (!member.getPassword().equals(password)) {
      throw new AuthenticationException();
    }

    String token = jwtTokenProvider.createToken(member.getEmail());

    return new TokenResponse(token);
  }
}
