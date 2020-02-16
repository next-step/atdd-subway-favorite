package atdd.user.application;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.entity.User;
import atdd.user.repository.UserRepository;

@Service
public class UserService {
  private BCryptPasswordEncoder passwordEncoder;
  private UserRepository userRepository;

  public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public UserResponseView SignupUser(CreateUserRequestView createUserRequestView) {
    String encryptedUserPassword = passwordEncoder.encode(createUserRequestView.getPassword());
    User createdUser = userRepository.save(new User(
        createUserRequestView.getEmail(),
        createUserRequestView.getName(),
        encryptedUserPassword
        )
      );
    return new UserResponseView(createdUser.getId(), createdUser.getEmail(), createdUser.getName());
  }

  public Optional<UserResponseView> RetrieveUser(Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (!optionalUser.isPresent()) {
      return Optional.empty();
    }
    User user = optionalUser.get();
    UserResponseView userResponseView = new UserResponseView(
        user.getId(),
        user.getEmail(),
        user.getName()
        );
    return Optional.of(userResponseView);
  }

  public void DeleteUser(Long id) {
    userRepository.deleteById(id);
    return;
  }

  @Autowired
  public UserService(UserRepository userRepository) {
    passwordEncoder = new BCryptPasswordEncoder();
    this.userRepository = userRepository;
  }

}
