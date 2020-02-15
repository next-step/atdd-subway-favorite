package atdd.path.web;

import atdd.path.application.UserService;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UserController {
    private static final String BASE_URI="/user";
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping(BASE_URI)
    public ResponseEntity<UserResponseView> create(@RequestBody CreateUserRequestView request){
        UserResponseView response=userService.createUser(request);
        return ResponseEntity
                .created(URI.create(BASE_URI+"/"+response.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
