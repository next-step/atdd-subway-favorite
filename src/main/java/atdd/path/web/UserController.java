package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private static final String BASE_URI = "/user";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(BASE_URI)
    public ResponseEntity<UserResponseView> create(@RequestBody @Valid CreateUserRequestView request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        if (checkExistingUser(request)) {
            return ResponseEntity
                    .ok()
                    .build();
        }

        UserResponseView response = userService.createUser(request);
        return ResponseEntity
                .created(URI.create(BASE_URI + "/" + response.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping(BASE_URI + "/{id}")
    public ResponseEntity<UserResponseView> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    private boolean checkExistingUser(CreateUserRequestView request) {
        return userService.checkUserExist(request);
    }
}
