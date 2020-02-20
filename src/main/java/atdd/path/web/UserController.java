package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    final String ACCESS_TOKEN_HEADER = "Authorization";

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid final CreateUserRequestView view) {
        User user = userService.createUser(view);

        return ResponseEntity.created(URI.create("/users/" + user.getId()))
                .body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable final long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody final LoginRequestView view) {
        String token = userService.login(view.getEmail(), view.getPassword());

        return ResponseEntity.noContent()
                .header(ACCESS_TOKEN_HEADER, token).build();
    }
}
