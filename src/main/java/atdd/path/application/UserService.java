package atdd.path.application;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public User createUser(CreateUserRequestView view) {
        User user = User.builder()
                .id(view.getId())
                .email(view.getEmail())
                .name(view.getName())
                .password(view.getPassword())
                .build();

        User newUser = userDao.save(user);

        return newUser;
    }

    public User retrieveUser(Long id) {

        Optional<User> findUser = Optional.ofNullable(userDao.findById(id));

        return findUser
                .map(user -> User.builder()
                        .id(findUser.get().getId())
                        .email(findUser.get().getEmail())
                        .name(findUser.get().getName())
                        .password(findUser.get().getPassword())
                        .build())
                .orElseGet(() -> null);
    }

    public void deleteById(Long id) {

        userDao.deleteById(id);
    }
}
