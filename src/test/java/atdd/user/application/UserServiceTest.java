package atdd.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.entity.User;
import atdd.user.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static atdd.user.TestConstant.*;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
  private UserService userService;

  @MockBean
  private BCryptPasswordEncoder passwordEncoder;

  @MockBean
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    this.userService = new UserService(passwordEncoder, userRepository);
  }

  @Test
  public void signupUser() {
    String encryptedUserPassword = USER_1_PASSWORD.toUpperCase();
    User expectUser = new User(1L, USER_1_EMAIL, USER_1_NAME, encryptedUserPassword);

    given(passwordEncoder.encode(USER_1_PASSWORD)).willReturn(encryptedUserPassword);
    given(userRepository.save(any(User.class))).willReturn(expectUser);

    CreateUserRequestView createUserRequestView = new CreateUserRequestView(USER_1_EMAIL, USER_1_NAME, USER_1_PASSWORD);
    UserResponseView result = userService.SignupUser(createUserRequestView);

    assertThat(result.getId()).isEqualTo(expectUser.getId());
    assertThat(result.getEmail()).isEqualTo(expectUser.getEmail());
    assertThat(result.getName()).isEqualTo(expectUser.getName());
  }
}
