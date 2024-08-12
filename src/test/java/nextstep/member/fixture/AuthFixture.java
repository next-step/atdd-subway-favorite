package nextstep.member.fixture;

import nextstep.security.service.JwtTokenAuthenticationService;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.security.payload.TokenResponse;
import nextstep.member.application.dto.UserInfoDto;
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
