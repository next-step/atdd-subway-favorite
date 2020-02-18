package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private UserDao userDao;

    public UserController(UserDao userDao){this.userDao = userDao;};

    @PostMapping()
    public ResponseEntity createUser(@RequestBody CreateUserRequestView view){
        User persistUser = userDao.save(view.toUser());
        return ResponseEntity
                .created(URI.create("/users/ + "+ persistUser.getId()))
                .body(UserResponseView.of(persistUser));
    }

}
