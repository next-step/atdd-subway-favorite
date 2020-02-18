package atdd.path.application;

import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import atdd.path.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseView createUser(UserRequestView userRequestView) {
        return UserResponseView.of(userRepository.save(userRequestView.toUser()));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponseView retrieveUser(HttpServletRequest request) {
        return UserResponseView.of(User.createBuilder().email("boorwonie@email.com").build());
    }
}
