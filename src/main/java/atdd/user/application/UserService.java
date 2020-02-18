package atdd.user.application;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import atdd.user.application.dto.AuthInfoView;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.entity.User;
import atdd.user.repository.UserRepository;

@Service
public class UserService {
  private BCryptPasswordEncoder passwordEncoder;
  private AuthService authService;
  private UserRepository userRepository;

  public UserResponseView SignupUser(CreateUserRequestView createUserRequestView) {
    String encryptedUserPassword = HashingPassword(createUserRequestView.getPassword());
    System.out.println(encryptedUserPassword);
    User createdUser = userRepository.save(
        new User(
          createUserRequestView.getEmail(),
          createUserRequestView.getName(),
          encryptedUserPassword
        )
      );
    return new UserResponseView(createdUser.getId(), createdUser.getEmail(), createdUser.getName());
  }

  private String HashingPassword(String password) {
    return passwordEncoder.encode(password);
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

  public Optional<AuthInfoView> LoginUser(LoginUserRequestView loginUserRequestView) {
    Optional<User> result = userRepository.findByEmail(
        loginUserRequestView.getEmail()
        );

    if (!result.isPresent()) {
      return Optional.empty();
    }
    User user = result.get();

    if( !passwordEncoder.matches(
          loginUserRequestView.getPassword(),
          user.getPassword())
        ) {
      return Optional.empty();
    }

    return Optional.of(
        authService.GenerateAuthToken(
          user.getEmail()
          ));
  }

  public void DeleteUser(Long id) {
    userRepository.deleteById(id);
    return;
  }

  public Optional<UserResponseView> RetrieveUserByAuthToken(AuthInfoView authInfoView) {
    Optional<String> authResult = authService.AuthUser(authInfoView);
    if(!authResult.isPresent()) {
      return Optional.empty();
    }

    String email = authResult.get();
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if(!optionalUser.isPresent()) {
      return Optional.empty();
    }
    User user = optionalUser.get();

    UserResponseView userResponseView = new UserResponseView(
        user.getId(), user.getEmail(), user.getName());
    return Optional.of(userResponseView);
  }

  public UserService(BCryptPasswordEncoder passwordEncoder, AuthService authService, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.authService = authService;
    this.userRepository = userRepository;
  }

  @Autowired
  public UserService(AuthService authService, UserRepository userRepository) {
    this.passwordEncoder = new BCryptPasswordEncoder();
    this.authService = authService;
    this.userRepository = userRepository;
  }

}
