package atdd.user.web;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import atdd.user.application.UserService;
import atdd.auth.LoginUser;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;

@RestController
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user")
  public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
    UserResponseView userResponseView = userService.signupUser(view);
    return ResponseEntity.created(
        URI.create("/user/" + userResponseView.getId().toString())
        )
        .body(userResponseView);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity retrieveUser(@PathVariable("id") Long id) {
    return ResponseEntity.ok(userService.retrieveUser(id));
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/user/me")
  public ResponseEntity retrieveUserByAuthToken(
      @LoginUser UserResponseView userResponseView) {
    return ResponseEntity.ok(userResponseView);
  }
}


