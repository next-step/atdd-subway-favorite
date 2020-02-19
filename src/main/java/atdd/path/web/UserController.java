package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.SignInUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.auth.JwtTokenDTO;
import atdd.path.auth.JwtTokenProvider;
import atdd.path.domain.User;
import atdd.path.repository.UserRepository;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.created(URI.create("/users/" + persistUser.getId()))
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

        return optionalUser.map(this::getUserResponse)
                           .orElseGet(this::notFoundResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);

        return noContentResponse();
    }

    @PostMapping("/login")
    public ResponseEntity signInUser(@RequestBody SignInUserRequestView view) {
        Optional<User> savedUser = userRepository.findByEmail(view.getEmail());

        return savedUser.map(user -> getTokenOrUnAuthResponse(user, view))
                        .orElseGet(this::noContentResponse);
    }


    @GetMapping("/me")
    public ResponseEntity showSignedInUser(@RequestHeader(value = "Authorization") String authorization) {
        String token = authorization.split(" ")[1];

        String parsedEmail = jwtTokenProvider.parseToken(token);

        Optional<User> optionalUser = userRepository.findByEmail(parsedEmail);

        return optionalUser.map(this::getUserResponse)
                           .orElseGet(this::notFoundResponse);
    }

    private ResponseEntity getUserResponse(User user) {
        return ResponseEntity.ok().body(UserResponseView.of(user));
    }

    private ResponseEntity getTokenOrUnAuthResponse(User user, SignInUserRequestView view) {
        boolean isValidateUser = user.getPassword().equals(view.getPassword());

        if (isValidateUser) {
            String accessToken = jwtTokenProvider.createToken(view.getEmail());
            String tokenType = "Bearer";

            JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(accessToken, tokenType);

            return ResponseEntity.ok().body(jwtTokenDTO);
        }
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity noContentResponse() {
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity notFoundResponse() {
        return ResponseEntity.notFound().build();
    }
}
