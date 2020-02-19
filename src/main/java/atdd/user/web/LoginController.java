package atdd.user.web;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Base64.Encoder;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import atdd.configure.JwtConfig;
import atdd.user.application.UserService;
import atdd.auth.application.dto.AuthInfoView;
import atdd.user.application.dto.LoginUserRequestView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@Controller
public class LoginController {
  private UserService userService;

  @Autowired
  public LoginController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity Login(@RequestBody LoginUserRequestView loginUserRequestView) {
    Optional<AuthInfoView> result = userService.LoginUser(loginUserRequestView);
    if (!result.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(result.get());
  }


  public LoginController() {
  }

}
