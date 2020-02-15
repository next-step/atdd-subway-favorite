package atdd.path.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
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

@RestController
public class UserController {
  @PostMapping("/user")
  public ResponseEntity createUser(@RequestBody CreateUserRequestView view) {
    UserResponseView userResponseView = new UserResponseView(
        1L, view.getEmail(), view.getName(), view.getPassword()
        );
    return ResponseEntity.created(URI.create("/user/1")).body(userResponseView);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity retrieveUser(@PathVariable("id") Long id) {
    UserResponseView userResponseView = new UserResponseView(
        1L, "user1@gmail.com", "손건", "asdf"
        );
    return ResponseEntity.ok(userResponseView);
  }
}


