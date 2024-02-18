package nextstep.api.auth.interfaces.controller;

import static org.springframework.http.ResponseEntity.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.application.dto.GithubLoginRequest;
import nextstep.api.auth.application.dto.TokenRequest;
import nextstep.api.auth.application.dto.TokenResponse;
import nextstep.api.auth.application.facade.AuthFacade;
import nextstep.api.auth.domain.dto.inport.GithubCodeResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthFacade authFacade;

	@PostMapping("/login/token")
	public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
		return ok(authFacade.createToken(request));
	}

	@PostMapping("/login/github")
	public ResponseEntity<TokenResponse> githubLogin(@RequestBody GithubCodeResponse codeResponse) {
		return ok(authFacade.githubLogin(GithubLoginRequest.from(codeResponse)));
	}
}
