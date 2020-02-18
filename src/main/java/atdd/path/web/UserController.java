package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.UserResponseDto;
import atdd.path.web.dto.UserCreateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(UserController.ROOT_URI)
public class UserController {
    public static final String ROOT_URI = "/users";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequestDto createRequestDto) {
        UserResponseDto responseDto = userService.create(createRequestDto.getEmail(), createRequestDto.getName(), createRequestDto.getPassword());
        return ResponseEntity.created(URI.create(UserController.ROOT_URI + "/" + responseDto.getId())).build();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}
