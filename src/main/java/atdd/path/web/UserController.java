package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.User.UserLoginRequestView;
import atdd.path.application.dto.User.UserSighUpRequestView;
import atdd.path.application.dto.User.UserSighUpResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import atdd.path.security.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private UserDao userDao;

    public UserController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

    @PostMapping("/sigh-up")
    public ResponseEntity signUp(@RequestBody UserSighUpRequestView user) {
        UserSighUpResponseView savedUser = userService.singUp(user);
        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginRequestView loginUser) {
        return ResponseEntity.ok().body(userService.login(loginUser));
    }

    @DeleteMapping("")
    public ResponseEntity delete(@LoginUser User user) {
        userDao.deleteById(user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity detail(@LoginUser User user) {
        return ResponseEntity.ok().body(userService.findById(user.getId()));
    }
}
