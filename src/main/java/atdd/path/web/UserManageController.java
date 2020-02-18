package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UserManageController {
    private UserDao userDao;

    public UserManageController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseView> createUser(@RequestBody CreateUserRequestView view) {
        User persistStation = userDao.save(view.toUser());
        return ResponseEntity
                .created(URI.create("/user" + persistStation.getId()))
                .body(UserResponseView.of(persistStation));
    }
}
