package atdd.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import atdd.user.application.UserService;
import atdd.user.application.dto.LoginUserRequestView;

@Controller
public class LoginController {
  private UserService userService;

  @Autowired
  public LoginController(UserService userService) {
    this.userService = userService;
  }

  public LoginController() {
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginUserRequestView loginUserRequestView) {
    return ResponseEntity.ok(
        userService.loginUser(loginUserRequestView));
  }


}
