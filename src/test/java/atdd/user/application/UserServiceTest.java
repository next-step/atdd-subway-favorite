package atdd.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import atdd.configure.JwtConfig;
import atdd.path.application.exception.NoDataException;
import atdd.auth.application.AuthService;
import atdd.auth.application.dto.AuthInfoView;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.entity.User;
import atdd.user.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import static atdd.user.TestConstant.*;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JwtConfig.class)
public class UserServiceTest {
  @Autowired
  private JwtConfig jwtConfig;

  @Autowired
  private UserRepository userRepository;

  private AuthService authService;

  private UserService userService;

  @BeforeEach
  void setUp() {
    this.authService = new AuthService(jwtConfig);
    this.userService = new UserService(authService, userRepository);
  }

  @Test
  public void signupUser() {
    CreateUserRequestView createUserRequestView = new CreateUserRequestView(USER_1_EMAIL, USER_1_NAME, USER_1_PASSWORD);

    UserResponseView result = userService.signupUser(createUserRequestView);

    Optional<User> findResult = userRepository.findById(result.getId());
    if (!findResult.isPresent()) {
      fail("회원가입한 유저를 찾을 수 없음");
      return;
    }
    User expectUser = findResult.get();

    assertThat(result.getId()).isEqualTo(expectUser.getId());
    assertThat(result.getEmail()).isEqualTo(expectUser.getEmail());
    assertThat(result.getName()).isEqualTo(expectUser.getName());
  }

  @Test
  public void retriveUser() {
    CreateUserRequestView createUserRequestView = new CreateUserRequestView(USER_1_EMAIL, USER_1_NAME, USER_1_PASSWORD);

    UserResponseView expectResult = userService.signupUser(createUserRequestView);

    UserResponseView result = userService.retrieveUser(expectResult.getId());

    assertThat(result.getId()).isEqualTo(expectResult.getId());
    assertThat(result.getEmail()).isEqualTo(expectResult.getEmail());
    assertThat(result.getName()).isEqualTo(expectResult.getName());
  }

  @Test
  public void deleteUser() {
    CreateUserRequestView createUserRequestView = new CreateUserRequestView(USER_1_EMAIL, USER_1_NAME, USER_1_PASSWORD);
    UserResponseView expectResult = userService.signupUser(createUserRequestView);

    userService.deleteUser(expectResult.getId());


    try {
      userService.retrieveUser(expectResult.getId());
    } catch (NoDataException e) {
      return;
    } catch (Exception e) {
      fail("Unknown Exception : " + e.toString());
      return;
    }

    fail("회원 탈퇴가 정상적으로 되지 않음");
    return;
  }

  @Test
  public void loginUser() {
    CreateUserRequestView createUserRequestView = new CreateUserRequestView(USER_1_EMAIL, USER_1_NAME, USER_1_PASSWORD);
    UserResponseView expectResult = userService.signupUser(createUserRequestView);

    LoginUserRequestView loginUserRequestView = new LoginUserRequestView(USER_1_EMAIL, USER_1_PASSWORD);

    try {
      AuthInfoView result = userService.loginUser(loginUserRequestView);
    } catch (Exception e) {
      fail("로그인 실패");
      return;
    }
  }

  @Test
  public void retriveUserByAuthToken() {
    CreateUserRequestView createUserRequestView = new CreateUserRequestView(USER_1_EMAIL, USER_1_NAME, USER_1_PASSWORD);
    UserResponseView expectResult = userService.signupUser(createUserRequestView);

    AuthInfoView authInfoView = authService.generateAuthToken(expectResult.getEmail());

    UserResponseView result = userService.retrieveUserByAuthToken(authInfoView);

    assertThat(result.getId()).isEqualTo(expectResult.getId());
    assertThat(result.getEmail()).isEqualTo(expectResult.getEmail());
    assertThat(result.getName()).isEqualTo(expectResult.getName());
  }
}
