package atdd.user.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

import static atdd.Constant.USER_BASE_URI;

@RestController
@RequestMapping(USER_BASE_URI)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseView> create(@RequestBody @Valid CreateUserRequestView request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        User user = isExistingUser(request);
        if (user != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        UserResponseView response = userService.createUser(request);
        return ResponseEntity
                .created(URI.create("/" + response.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseView> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity
                .notFound()
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseView> retrieveInfo(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        User user = userService.findByEmail(email);
        UserResponseView response = new UserResponseView(user.getEmail(), user.getName(), user.getPassword());
        return ResponseEntity
                .ok()
                .body(response);
    }

    private User isExistingUser(CreateUserRequestView request) {
        User user = userService.findByEmail(request.getEmail());
        return user;
    }
}
