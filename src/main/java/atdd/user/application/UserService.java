package atdd.user.application;

import atdd.exception.InvalidUserException;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {
    @Autowired
    private JwtUtils jwtUtils;

    public UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(CreateUserRequestView view) {
        User user = view.toUSer();
        user.encryptPassword();

        return userDao.save(user);
    }

    public void deleteUser(final long id) {
        userDao.deleteById(id);
    }

    public String login(final String email, final String password) {
        User findUser;
        try {
            findUser = userDao.findByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new InvalidUserException();
        }

        findUser.checkPassword(password);

        return jwtUtils.createToken(findUser.getEmail());
    }

    public User myInfo(final String email) {
        return userDao.findByEmail(email);
    }
}
