package atdd.path.application;

import atdd.path.application.dto.UserSighUpRequestView;
import atdd.path.dao.UserDao;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<Station> singUp(UserSighUpRequestView user) {
        userDao.findByEmail(user.getEmail());

        return new ArrayList<>();
    }

}
