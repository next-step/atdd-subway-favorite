package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.LoginRequestView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody LoginRequestView loginRequestView) {
        return ResponseEntity.ok(userService.login(loginRequestView));
    }
}
