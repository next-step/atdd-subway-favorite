package atdd.user.application;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atdd.auth.application.AuthService;
import atdd.auth.application.dto.AuthInfoView;
import atdd.path.application.exception.NoDataException;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.application.exception.UnauthorizedException;
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

  public UserResponseView RetrieveUser(Long id) {
    User user = userRepository.findById(id)
      .orElseThrow(NoDataException::new);

    UserResponseView userResponseView = new UserResponseView(
        user.getId(),
        user.getEmail(),
        user.getName()
        );
    return userResponseView;
  }

  public Optional<AuthInfoView> LoginUser(LoginUserRequestView loginUserRequestView) {
    Optional<User> result = userRepository.findByEmail(
        loginUserRequestView.getEmail()
        );

    User user = result.orElseThrow(NoDataException::new);

    if( !loginUserRequestView.getPassword()
        .equals(user.getPassword())) {
      throw new UnauthorizedException();
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

  public UserResponseView RetrieveUserByAuthToken(AuthInfoView authInfoView) {
    String email = authService.AuthUser(authInfoView);

    User user = userRepository
      .findByEmail(email)
      .orElseThrow(NoDataException::new);

    UserResponseView userResponseView = new UserResponseView(
        user.getId(), user.getEmail(), user.getName());
    return userResponseView;
  }


}
