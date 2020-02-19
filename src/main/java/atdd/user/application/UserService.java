package atdd.user.application;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atdd.auth.application.AuthService;
import atdd.auth.application.dto.AuthInfoView;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.entity.User;
import atdd.user.repository.UserRepository;

@Service
public class UserService {
  private AuthService authService;
  private UserRepository userRepository;

  @Autowired
  public UserService(AuthService authService, UserRepository userRepository) {
    this.authService = authService;
    this.userRepository = userRepository;
  }

  public UserResponseView SignupUser(CreateUserRequestView createUserRequestView) {
    User createdUser = userRepository.save(
        new User(
          createUserRequestView.getEmail(),
          createUserRequestView.getName(),
          createUserRequestView.getPassword()
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

  public Optional<AuthInfoView> LoginUser(LoginUserRequestView loginUserRequestView) {
    Optional<User> result = userRepository.findByEmail(
        loginUserRequestView.getEmail()
        );

    if (!result.isPresent()) {
      return Optional.empty();
    }
    User user = result.get();

    if( !loginUserRequestView.getPassword()
        .equals(user.getPassword())) {
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


}
