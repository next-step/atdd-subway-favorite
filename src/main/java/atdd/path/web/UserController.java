package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/user")
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
}
