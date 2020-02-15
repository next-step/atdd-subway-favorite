package atdd.path.application;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import atdd.path.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public UserResponseView createUser(CreateUserRequestView request){
        User createdUser=userRepository.save(request.toEntity());
        return UserResponseView.of(createdUser);
    }
}
