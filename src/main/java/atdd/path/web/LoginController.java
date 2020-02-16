package atdd.path.web;

import atdd.path.application.LoginService;
import atdd.path.application.dto.LoginRequestView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody LoginRequestView loginRequestView) {
        return ResponseEntity.ok(loginService.login(loginRequestView));
    }
}
