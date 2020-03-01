package atdd.path.web;

import atdd.path.application.JwtTokenProvider;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.TokenResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static atdd.path.application.base.BaseUriConstants.LOGIN_SIGN_IN_URL;

@RestController
@RequestMapping(LOGIN_SIGN_IN_URL)
public class LoginController {

    private static final String JWT_TOKEN_TYPE = "Bearer";
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<TokenResponseView> login(@RequestBody CreateUserRequestView view) {
        User findUser = userDao.findByEmailAndPassword(view.getEmail(), view.getPassword());
        String accessToken = jwtTokenProvider.createToken(findUser.getEmail());
        return ResponseEntity.ok().body(TokenResponseView.of(accessToken, JWT_TOKEN_TYPE));
    }
}
