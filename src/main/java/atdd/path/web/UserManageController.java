package atdd.path.web;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.dao.UserDao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManageController {
    private UserDao userDao;

    public UserManageController(UserDao userDao) {
        this.userDao = userDao;
    }
    @PostMapping("/user")
    public void CreateUser(@RequestBody CreateUserRequestView view) {
        userDao.save(view.toUser());
    }
}
