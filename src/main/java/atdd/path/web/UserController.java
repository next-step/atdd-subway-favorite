package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.UserLoginResponseView;
import atdd.path.application.dto.UserSighUpRequestView;
import atdd.path.application.dto.UserSighUpResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
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

    @PostMapping("")
    public ResponseEntity signUp(@RequestBody UserSighUpRequestView user) {
        UserSighUpResponseView savedUser = userService.singUp(user);
        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(savedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        userDao.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User loginUser) {
        return ResponseEntity.ok().body(UserLoginResponseView.builder().accessToken("toekn").tokenType("Bearer").build());
    }
}
