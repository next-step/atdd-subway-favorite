package atdd.path.application;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.repository.UserRepository;

@Service
public class UserService {
  private BCryptPasswordEncoder passwordEncoder;
  private UserRepository userRepository;

  public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public UserResponseView SignupUser(CreateUserRequestView createUserRequestView) {

    return null;
  }

}
