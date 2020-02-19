package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.SignInUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.auth.JwtTokenDTO;
import atdd.path.auth.JwtTokenProvider;
import atdd.path.domain.User;
import atdd.path.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    public UserController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
        User persistUser = userRepository.save(view.toUser());
        return ResponseEntity
                .created(URI.create("/users/" + persistUser.getId()))
                .body(UserResponseView.of(persistUser));
    }

    @GetMapping
    public ResponseEntity showUsers() {
        List<User> users = new ArrayList<User>();

        userRepository.findAll().forEach(users::add);

        return ResponseEntity.ok().body(UserResponseView.listOf(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity showUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser
                .map(user -> ResponseEntity.ok().body(UserResponseView.of(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity signInUser(@RequestBody SignInUserRequestView view) {
        String accessToken = jwtTokenProvider.createToken(view.getEmail());
        String tokenType = "Bearer";

        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(accessToken, tokenType);

        return ResponseEntity.ok().body(jwtTokenDTO);
    }
}
