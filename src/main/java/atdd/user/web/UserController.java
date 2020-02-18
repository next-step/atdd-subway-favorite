package atdd.user.web;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import atdd.user.application.UserService;
import atdd.user.application.dto.AuthInfoView;
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
    Optional<UserResponseView> result = userService.RetrieveUser(id);
    if(!result.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(result.get());
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity deleteUser(@PathVariable("id") Long id) {
    userService.DeleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/user/me")
  public ResponseEntity retrieveUserByAuthToken(
      @RequestHeader(value=HttpHeaders.AUTHORIZATION) String authToken) {
    AuthInfoView authInfoView = new AuthInfoView(authToken);

    Optional<UserResponseView> result = userService.RetrieveUserByAuthToken(authInfoView);
    if(!result.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(result.get());
  }

  @Autowired
  public UserController(UserService userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }

  public UserController() {
  }

}


