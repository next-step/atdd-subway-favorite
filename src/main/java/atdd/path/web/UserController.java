package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
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
    public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
        User persistUser = userService.createUser(view);

        return ResponseEntity
                .created(URI.create("/users/" + persistUser.getId()))
                .body(UserResponseView.of(persistUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity retrieveUser(@PathVariable Long id) {
        User persistUser = userService.retrieveUser(id);
        return ResponseEntity.ok().body(UserResponseView.of(persistUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
