package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
        User persistUser = userService.createUser(view);

        return ResponseEntity
                .created(URI.create("/stations/" + persistUser.getId()))
                .body(UserResponseView.of(persistUser));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity retrieveUser(@PathVariable Long id) {
        try {
            User persistUser = userService.retrieveUser(id);
            return ResponseEntity.ok().body(UserResponseView.of(persistUser));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
