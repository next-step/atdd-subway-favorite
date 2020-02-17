package atdd.user.web;

import java.util.Date;
import java.util.Map;
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
import atdd.user.application.dto.AuthInfoView;
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
  private JwtConfig jwtConfig;
  private UserService userService;

  public LoginController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity Login(@RequestBody LoginUserRequestView loginUserRequestView, @RequestHeader Map<String, String> headers) {
    byte[] rawKey = Decoders.BASE64.decode(jwtConfig.getKey());
    SecretKey key = Keys.hmacShaKeyFor(rawKey);

    Claims claims = Jwts.claims().setSubject(loginUserRequestView.getEmail());

    Date now = new Date();
    Date validity = new Date(now.getTime() + 20000L);

    String tokenType = "bearer";
    String accessToken = Jwts.builder()
      .setClaims(claims)
      .setIssuedAt(now)
      .setExpiration(validity)
      .signWith(key)
      .compact();

    return ResponseEntity.ok(new AuthInfoView(accessToken, tokenType));
  }

  @Autowired
  public LoginController(JwtConfig jwtConfig, UserService userService) {
    this.jwtConfig = jwtConfig;
    this.userService = userService;
  }

  public LoginController() {
  }

}
