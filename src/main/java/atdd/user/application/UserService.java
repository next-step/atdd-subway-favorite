package atdd.user.application;


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

  public UserResponseView signupUser(CreateUserRequestView createUserRequestView) {
    User createdUser = userRepository.save(
        new User(
          createUserRequestView.getEmail(),
          createUserRequestView.getName(),
          createUserRequestView.getPassword()
        )
      );
    return new UserResponseView(createdUser.getId(), createdUser.getEmail(), createdUser.getName());
  }

  public UserResponseView retrieveUser(Long id) {
    User user = userRepository.findById(id)
      .orElseThrow(NoDataException::new);

    UserResponseView userResponseView = new UserResponseView(
        user.getId(),
        user.getEmail(),
        user.getName()
        );
    return userResponseView;
  }

  public AuthInfoView loginUser(LoginUserRequestView loginUserRequestView) {
    User user = userRepository.findByEmail(
        loginUserRequestView.getEmail()
        ).orElseThrow(NoDataException::new);


    if( !loginUserRequestView.getPassword()
        .equals(user.getPassword())) {
      throw new UnauthorizedException();
    }

    return authService.generateAuthToken(
        user.getEmail());
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
    return;
  }

  public UserResponseView retrieveUserByAuthToken(AuthInfoView authInfoView) {
    String email = authService.authUser(authInfoView);

    User user = userRepository
      .findByEmail(email)
      .orElseThrow(NoDataException::new);

    UserResponseView userResponseView = new UserResponseView(
        user.getId(), user.getEmail(), user.getName());
    return userResponseView;
  }
}
