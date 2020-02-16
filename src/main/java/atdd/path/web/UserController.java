package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping
    public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
        Long id = 1L;

        String userEmail = "boorwonie@email.com";
        String userName = "브라운";
        String password = "subway";

        User user = new User(userEmail, userName, password);
        return ResponseEntity.created(URI.create("/lines/" + id)).body(UserResponseView.of(user));
    }

    @GetMapping
    public ResponseEntity showUsers() {
        List<User> persistUsers = new ArrayList<User>();

        String userEmail = "boorwonie@email.com";
        String userName = "브라운";
        String password = "subway";

        User user = new User(userEmail, userName, password);
        persistUsers.add(user);
        return ResponseEntity.ok().body(UserResponseView.listOf(persistUsers));
    }
}
