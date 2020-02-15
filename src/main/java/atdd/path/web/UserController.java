package atdd.path.web;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.entity.User;
import atdd.path.repository.UserRepository;

@RestController
public class UserController {
  private UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/user")
  public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
    User createdUser = userRepository.save(new User(
          view.getEmail(),
          view.getName(),
          view.getPassword()
          ));

    UserResponseView userResponseView = new UserResponseView(
        createdUser.getId(),
        createdUser.getEmail(),
        createdUser.getName()
        );
    return ResponseEntity.created(
        URI.create("/user/" + createdUser.getId().toString()))
      .body(userResponseView);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity retrieveUser(@PathVariable("id") Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      UserResponseView userResponseView = new UserResponseView(
          user.getId(),
          user.getEmail(),
          user.getName()
          );
      return ResponseEntity.ok(userResponseView);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity deleteUser(@PathVariable("id") Long id) {
    userRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}


