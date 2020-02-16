package atdd.user.web;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import atdd.user.application.UserService;
import atdd.user.application.dto.AuthInfoView;
import atdd.user.application.dto.LoginUserRequestView;

@Controller
public class LoginController {
  private UserService userService;

  public LoginController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity Login(@RequestBody LoginUserRequestView loginUserRequestView, @RequestHeader Map<String, String> headers) {
    return ResponseEntity.ok(new AuthInfoView("", ""));
  }

}
