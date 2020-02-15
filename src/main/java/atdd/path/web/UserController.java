package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping(value = "")
    public ResponseEntity<UserResponseView> createUser(@RequestBody CreateUserRequestView view) {
        User savedUser = userDao.save(view.toUser());
        return ResponseEntity.created(URI.create("/" + savedUser.getId()))
                .body(UserResponseView.of(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseView> findById(@PathVariable Long id) {
        try {
            User findUser = userDao.findById(id);
            return ResponseEntity.ok().body(UserResponseView.of(findUser));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseView> deleteById(@PathVariable Long id) {
        userDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
