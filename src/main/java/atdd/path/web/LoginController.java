package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.LoginReponseView;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.domain.User;
import atdd.path.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URI;

import static atdd.path.web.Constant.LOGIN_BASE_URI;

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
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 이메일입니다."));
        boolean isMatch = user.getPassword().equals(request.getPassword());
        if (!isMatch) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }
        String accessToken = jwtTokenProvider.createToken(request.getEmail());
        String tokenType = "bearer";
        return ResponseEntity
                .created(URI.create("/oauth/token"))
                .body(new LoginReponseView(accessToken, tokenType));
    }
}
