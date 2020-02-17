package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.domain.entity.User;
import atdd.path.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
        User user = view.toUSer();

        userRepository.save(user);

        return ResponseEntity.created(URI.create("/users/" + user.getId()))
                .body(user);
    }
}
