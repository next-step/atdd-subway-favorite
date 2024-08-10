package nextstep.member.fixture;

import nextstep.member.application.JwtTokenAuthenticationService;
import nextstep.member.application.MemberService;
import nextstep.member.application.UserDetailsServiceImpl;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.UserInfoDto;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthFixture {

  private static final String PASSWORD = "password";
  private final MemberService memberService;
  private final JwtTokenAuthenticationService jwtTokenAuthenticationService;

  public AuthFixture(MemberService memberService,
      JwtTokenAuthenticationService jwtTokenAuthenticationService) {
    this.memberService = memberService;
    this.jwtTokenAuthenticationService = jwtTokenAuthenticationService;
  }

  public String getMemberAccessToken(final String email) {
    memberService.createMember(new MemberRequest(email, PASSWORD, 29));
    TokenResponse response = jwtTokenAuthenticationService.createToken(
        new UserInfoDto(email, PASSWORD));
    return response.getAccessToken();
  }
}
