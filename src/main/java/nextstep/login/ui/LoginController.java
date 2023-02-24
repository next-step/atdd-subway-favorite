package nextstep.login.ui;

import nextstep.login.application.AuthenticationService;
import nextstep.login.application.dto.LoginRequest;
import nextstep.login.application.dto.LoginResponse;
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
    ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok().body(loginResponse);
    }
}
