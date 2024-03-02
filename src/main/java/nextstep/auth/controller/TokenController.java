package nextstep.auth.controller;

import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login/token")
    public TokenResponse createToken(@RequestBody TokenRequest request) {
        return tokenService.createToken(request.getEmail(), request.getPassword());
    }

    @PostMapping("/login/github")
    public TokenResponse createTokenWithGithub(@RequestBody GithubAccessTokenRequest request) {
        return tokenService.createTokenWithGithubUser(request.getCode());
    }
}
