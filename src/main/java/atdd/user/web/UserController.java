package atdd.user.web;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.entity.User;
import atdd.user.repository.UserRepository;

@RestController
public class UserController {
  private UserService userService;
  private UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/user")
  public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
    UserResponseView userResponseView = userService.SignupUser(view);
    return ResponseEntity.created(
        URI.create("/user/" + userResponseView.getId().toString())
        )
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

  @Autowired
  public UserController(UserService userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }

  public UserController() {
  }

}


