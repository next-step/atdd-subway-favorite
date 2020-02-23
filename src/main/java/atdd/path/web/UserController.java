package atdd.path.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.UserRequestView;
import atdd.user.application.dto.UserResponseView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequestView userRequestView) {
        UserResponseView savedUser = userService.createUser(userRequestView);
        return ResponseEntity.created(URI.create("users/" + savedUser.getId()))
                .body(savedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("me")
    public ResponseEntity retrieveUser(HttpServletRequest request) {
        String requestToken = request.getHeader("Authorization");
        return ResponseEntity.ok().body(userService.retrieveUser(requestToken));
    }
}
