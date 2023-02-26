package nextstep.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberAuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;

@RestController
@RequiredArgsConstructor
public class MemberAuthController {

	private final MemberAuthService memberAuthService;

	@PostMapping("/login/token")
	public ResponseEntity<TokenResponse> createMemberToken(@RequestBody TokenRequest tokenRequest) {
		TokenResponse token = memberAuthService.createMemberToken(tokenRequest);
		return ResponseEntity.ok().body(token);
	}
}
