package atdd.path.web;

import atdd.path.application.base.BaseUriConstants;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(BaseUriConstants.USER_BASE_URL)
public class UserController {

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping
    public ResponseEntity<UserResponseView> createUser(@RequestBody CreateUserRequestView view) {
        User savedUser = userDao.save(view.toUser());
        return ResponseEntity.created(URI.create("/" + savedUser.getId()))
                .body(UserResponseView.of(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseView> findById(@PathVariable Long id) {
        User findUser = userDao.findById(id);
        return ResponseEntity.ok().body(UserResponseView.of(findUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseView> deleteById(@PathVariable Long id) {
        userDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
