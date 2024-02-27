package subway.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import subway.dto.member.GithubLoginRequest;
import subway.dto.member.TokenRequest;
import subway.dto.member.TokenResponse;

@RestController
public class TokenController {
	private final TokenService tokenService;

	public TokenController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@PostMapping("/login/token")
	public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
		TokenResponse response = tokenService.createToken(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login/github")
	public ResponseEntity<TokenResponse> createTokenByGitHub(@RequestBody GithubLoginRequest request) {
		TokenResponse response = tokenService.createTokenByGitHub(request.getCode());
		return ResponseEntity.ok(response);
	}
}
