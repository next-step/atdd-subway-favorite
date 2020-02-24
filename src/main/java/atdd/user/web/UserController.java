package atdd.user.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.domain.User;
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

    @GetMapping("/my-info")
    public ResponseEntity myInfo(@LoginUser String email) {
        return ResponseEntity.ok(userService.myInfo(email));
    }
}
