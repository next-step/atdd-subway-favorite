package atdd.path.application;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import atdd.path.domain.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseView createUser(CreateUserRequestView request) {
        User createdUser = userRepository.save(request.toEntity());
        return UserResponseView.of(createdUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 역이 존재하지 않습니다."));
        userRepository.delete(user);
    }
}
