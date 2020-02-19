package atdd.path.web;

import atdd.path.application.JwtTokenProvider;
import atdd.path.application.dto.AccessTokenResponseView;
import atdd.path.application.dto.CreateAccessTokenRequestView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	private JwtTokenProvider jwtTokenProvider;

	public LoginController(final JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping(value = "/oauth/token")
	public ResponseEntity login(@RequestBody CreateAccessTokenRequestView request) {
		String token = jwtTokenProvider.createToken(request.getEmail());
		AccessTokenResponseView response = AccessTokenResponseView.of(token);
		return ResponseEntity.ok(response);
	}
}
