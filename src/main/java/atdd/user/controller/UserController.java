package atdd.user.controller;

import atdd.security.LoginUserInfo;
import atdd.user.dto.AccessToken;
import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import atdd.user.service.AuthorizationService;
import atdd.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(UserController.ROOT_URI)
public class UserController {

    public static final String ROOT_URI = "/users";

    private final UserService userService;
    private final AuthorizationService authorizationService;

    public UserController(UserService userService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateRequestDto requestDto) {
        final UserResponseDto responseDto = userService.create(requestDto);
        final URI uri = UriComponentsBuilder.fromUriString(ROOT_URI + "/" + responseDto.getId()).build().toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

    @PostMapping("/login")
    public AccessToken login(@RequestParam String email, @RequestParam String password) {
        return authorizationService.authorize(email, password);
    }

    @GetMapping("/me")
    public UserResponseDto getMyInfo(LoginUserInfo loginUserInfo) {
        return UserResponseDto.of(
                loginUserInfo.getId(),
                loginUserInfo.getEmail(),
                loginUserInfo.getName(),
                loginUserInfo.getPassword()
        );
    }

}
