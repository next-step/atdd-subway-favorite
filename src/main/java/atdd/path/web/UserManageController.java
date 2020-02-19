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
@RequestMapping("/user")
public class UserManageController {
    private UserDao userDao;

    public UserManageController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping
    public ResponseEntity<UserResponseView> createUser(@RequestBody CreateUserRequestView view) {
        User persistStation = userDao.save(view.toUser());
        return ResponseEntity
                .created(URI.create("/user" + persistStation.getId()))
                .body(UserResponseView.of(persistStation));
    }


    @GetMapping("/{id}")
    public ResponseEntity retrieveUser(@PathVariable Long id) {
        try {
            User persistUser = userDao.findById(id);
            return ResponseEntity.ok().body(UserResponseView.of(persistUser));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        userDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
