package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid CreateUserRequestView view) {
        User user = userService.createUser(view);

        return ResponseEntity.created(URI.create("/users/" + user.getId()))
                .body(user);
    }
}
