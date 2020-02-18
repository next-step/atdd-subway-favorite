package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.UserSighUpRequestView;
import atdd.path.application.dto.UserSighUpResponseView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity signUp(@RequestBody UserSighUpRequestView user) {
        UserSighUpResponseView savedUser = userService.singUp(user);
        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(savedUser);
    }

    @PostMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
