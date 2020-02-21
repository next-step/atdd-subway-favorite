package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.UserRequestView;
import atdd.path.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        User createdUser = userService.create(user);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") User user) {

        if (user == null)
            return ResponseEntity.notFound().build();

        userService.delete(user);

        return ResponseEntity.noContent().build();
    }

}
