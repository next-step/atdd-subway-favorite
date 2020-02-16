package atdd.path.web;

import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
