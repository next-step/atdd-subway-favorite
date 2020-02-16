package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.UserRequestView;
import atdd.path.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequestView requestView) {
        User user = requestView.toEntity();
        return ResponseEntity.created(URI.create("/users/" + user.getId())).build();
    }
}
