package nextstep.member.ui;

import nextstep.security.service.JwtTokenAuthenticationService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.security.payload.TokenResponse;
import nextstep.member.application.dto.UserInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

  private final JwtTokenAuthenticationService jwtTokenAuthenticationService;

  public TokenController(JwtTokenAuthenticationService jwtTokenAuthenticationService) {
    this.jwtTokenAuthenticationService = jwtTokenAuthenticationService;
  }

  @PostMapping("/login/token")
  public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
    TokenResponse response = jwtTokenAuthenticationService.createToken(
        new UserInfoDto(request.getEmail(), request.getPassword()));
    return ResponseEntity.ok(response);
  }
}
