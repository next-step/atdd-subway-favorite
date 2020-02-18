package atdd.path.application;

import atdd.path.application.dto.UserSighUpRequestView;
import atdd.path.application.exception.ExistUserException;
import atdd.path.dao.UserDao;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User singUp(UserSighUpRequestView newUser) {
        List<Map<String, Object>> user = userDao.findByEmail(newUser.getEmail());

        if (user != null) {
            throw new ExistUserException();
        }

        return userDao.save(UserSighUpRequestView.toEntity(newUser));
    }
}
