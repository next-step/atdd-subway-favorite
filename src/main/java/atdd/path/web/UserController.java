package atdd.path.web;

import atdd.path.application.JwtTokenProvider;
import atdd.path.application.base.BaseUriConstants;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.application.exception.InvalidJwtAuthenticationException;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping(BaseUriConstants.USER_BASE_URL)
public class UserController {

    private final UserDao userDao;
    private JwtTokenProvider jwtTokenProvider;

    public UserController(UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<UserResponseView> createUser(@RequestBody CreateUserRequestView view) {
        User savedUser = userDao.save(view.toUser());
        return ResponseEntity.created(URI.create("/" + savedUser.getId()))
                .body(UserResponseView.of(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseView> findById(@PathVariable Long id) {
        User findUser = userDao.findById(id);
        return ResponseEntity.ok().body(UserResponseView.of(findUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseView> deleteById(@PathVariable Long id) {
        userDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseView> retrieveUser(HttpServletRequest request) {
        String extractEmail = extractEmail(request);
        User findUser = userDao.findByEmail(extractEmail);
        return ResponseEntity.ok().body(UserResponseView.of(findUser));
    }

    private String extractEmail(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (StringUtils.isEmpty(token) || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid token");
        }
        return jwtTokenProvider.getUserEmail(token);
    }
}
