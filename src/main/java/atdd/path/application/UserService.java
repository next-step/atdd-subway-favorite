package atdd.path.application;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(CreateUserRequestView view) {
        User user = User.builder()
                .email(view.getEmail())
                .name(view.getName())
                .password(view.getPassword())
                .build();

        User newUser = userDao.save(user);

        return newUser;
    }

    public User retrieveUser(Long id) {

        return userDao.findById(id);
    }
}
