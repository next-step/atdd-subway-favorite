package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import atdd.path.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
        User persistUser = userRepository.save(view.toUser());
        return ResponseEntity.created(URI.create("/lines/" + persistUser.getId())).body(UserResponseView.of(persistUser));
    }

    @GetMapping
    public ResponseEntity showUsers() {
        List<User> users = new ArrayList<User>();

        userRepository.findAll().forEach(users::add);

        return ResponseEntity.ok().body(UserResponseView.listOf(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity showUser(@PathVariable Long id) {
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
