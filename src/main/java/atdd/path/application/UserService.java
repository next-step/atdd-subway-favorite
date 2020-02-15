package atdd.path.application;

import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserResponseView createUser(UserRequestView userRequestView) {
        return UserResponseView.of(userDao.save(userRequestView.toUser()));
    }
}
