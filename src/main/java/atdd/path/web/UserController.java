package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequestView userRequestView) {
        UserResponseView savedUser = userService.createUser(userRequestView);
        return ResponseEntity.created(URI.create("user/" + savedUser.getId()))
                .body(savedUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
