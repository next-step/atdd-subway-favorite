package nextstep.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login/token")
	public ResponseEntity<AuthResponse> createToken(@RequestBody AuthRequest authRequest) {
		AuthResponse token = authService.createToken(authRequest);
		return ResponseEntity.ok().body(token);
	}
}
