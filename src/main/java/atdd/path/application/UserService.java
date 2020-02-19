package atdd.path.application;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.domain.entity.User;
import atdd.path.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(CreateUserRequestView view) {
        User user = view.toUSer();

        String hashPwd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        user.setPassword(hashPwd);

        return userRepository.save(user);
    }

    public void deleteUser(final long id) {
        userRepository.deleteById(id);
    }
}
