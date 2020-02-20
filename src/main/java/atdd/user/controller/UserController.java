package atdd.user.controller;

import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import atdd.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping(UserController.ROOT_URI)
public class UserController {

    public static final String ROOT_URI = "/users";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateRequestDto requestDto) {
        final UserResponseDto responseDto = userService.create(requestDto);
        final URI uri = UriComponentsBuilder.fromUriString(ROOT_URI + "/" + responseDto.getId()).build().toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

}
