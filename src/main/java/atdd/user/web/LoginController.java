package atdd.user.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.LoginReponseView;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.domain.User;
import atdd.user.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static atdd.Constant.LOGIN_BASE_URI;

@RestController
@RequestMapping(LOGIN_BASE_URI)
public class LoginController {
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    public LoginController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<LoginReponseView> login(@RequestBody LoginRequestView request) {
        User user = userService.findByEmail(request.getEmail());
        boolean isMatch = user.getPassword().equals(request.getPassword());
        if (!isMatch) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        LoginReponseView response = new LoginReponseView(request.getAccessToken(), request.getTokenType());
        return ResponseEntity
                .created(URI.create("/oauth/token"))
                .body(response);
    }
}
