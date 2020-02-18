package atdd.path.application;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.exception.InvalidEmailException;
import atdd.path.domain.entity.User;
import atdd.path.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(CreateUserRequestView view) {
        User newUser = view.toUSer();

        boolean checkEmail = checkEmail(newUser.getEmail());

        return userRepository.save(view.toUSer());
    }

    private boolean checkEmail(String email){
        throw new InvalidEmailException();
    }
}
