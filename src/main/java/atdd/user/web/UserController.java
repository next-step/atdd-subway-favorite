package atdd.user.web;

import atdd.user.application.UserService;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginResponseView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private UserDao userDao;
    private UserService userService;

    public UserController(UserDao userDao, UserService userService){
        this.userDao = userDao;
        this.userService = userService;};

    @PostMapping()
    public ResponseEntity createUser(@RequestBody CreateUserRequestView view){
        User persistUser = userDao.save(view.toUser());
        return ResponseEntity
                .created(URI.create("/users/"+ persistUser.getId()))
                .body(UserResponseView.of(persistUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        userDao.deleteByUserId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginUserRequestView loginData){
        LoginResponseView loginResponseView = userService.logIn(loginData);
        return ResponseEntity.ok().body(loginResponseView);
    }

}
