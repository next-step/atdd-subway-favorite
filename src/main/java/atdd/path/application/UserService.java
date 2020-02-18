package atdd.path.application;

import atdd.path.application.dto.UserSighUpRequestView;
import atdd.path.application.dto.UserSighUpResponseView;
import atdd.path.application.exception.ExistUserException;
import atdd.path.dao.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserSighUpResponseView singUp(UserSighUpRequestView newUser) {
        List<Map<String, Object>> user = userDao.findByEmail(newUser.getEmail());

        if (user != null) {
            throw new ExistUserException();
        }

        return UserSighUpResponseView.toDtoEntity(userDao.save(UserSighUpRequestView.toEntity(newUser)));
    }
}
