package atdd.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.entity.User;
import atdd.user.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import static atdd.user.TestConstant.*;

@DataJpaTest
@AutoConfigureDataJpa
public class UserServiceTest {
  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private UserRepository userRepository;

  private UserService userService;

  private BCryptPasswordEncoder passwordEncoder;


  @BeforeEach
  void setUp() {
    this.passwordEncoder = new BCryptPasswordEncoder();
    this.userService = new UserService(passwordEncoder, userRepository);
  }

  @Test
  public void signupUser() {
    CreateUserRequestView createUserRequestView = new CreateUserRequestView(USER_1_EMAIL, USER_1_NAME, USER_1_PASSWORD);

    UserResponseView result = userService.SignupUser(createUserRequestView);

    Optional<User> findResult = userRepository.findById(result.getId());
    if (!findResult.isPresent()) {
      fail("회원가입한 유저를 찾을 수 없음");
      return;
    }
    User expectUser = findResult.get();

    assertThat(result.getId()).isEqualTo(expectUser.getId());
    assertThat(result.getEmail()).isEqualTo(expectUser.getEmail());
    assertThat(result.getName()).isEqualTo(expectUser.getName());
    assertThat(createUserRequestView.getPassword()).isNotEqualTo(expectUser.getPassword());
  }
}
