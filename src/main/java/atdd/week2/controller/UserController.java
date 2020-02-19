package atdd.week2.controller;

import atdd.week2.dao.UserDao;
import atdd.week2.domain.User;
import atdd.week2.dto.UserRequestDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("")
    public ResponseEntity createUser(@RequestBody User user) {

        userDao.createUser(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Location", user.getEmail());

        return ResponseEntity.created(URI.create("/Location")).headers(httpHeaders).body(user);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
