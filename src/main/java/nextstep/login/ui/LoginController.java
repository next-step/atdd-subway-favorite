package nextstep.login.ui;

import nextstep.login.application.AuthenticationService;
import nextstep.login.application.dto.request.OAuthLoginRequest;
import nextstep.login.application.dto.request.TokenLoginRequest;
import nextstep.login.application.dto.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/token")
    ResponseEntity<LoginResponse> tokenLogin(@RequestBody final TokenLoginRequest tokenLoginRequest) {
        LoginResponse loginResponse = authenticationService.login(tokenLoginRequest.getEmail(), tokenLoginRequest.getPassword());
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/github")
    ResponseEntity<LoginResponse> githubLogin(@RequestBody final OAuthLoginRequest oAuthLoginRequest) {
        LoginResponse loginResponse = authenticationService.login(oAuthLoginRequest.getCode());
        return ResponseEntity.ok().body(loginResponse);
    }
}
