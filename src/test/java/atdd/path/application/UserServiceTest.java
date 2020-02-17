package atdd.path.application;

import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.domain.entity.User;
import atdd.path.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void createUser() {
        CreateUserRequestView view = CreateUserRequestView.builder()
                .email(USER_EMAIL1)
                .name(USER_NAME1)
                .password(USER_PASSWORD1).build();

        User user = view.toUSer();
        user.setId(1l);

        given(userRepository.save(view.toUSer())).willReturn(user);

        User newUser = userService.createUser(view);

        assertThat(newUser.getName()).isEqualTo(USER_EMAIL1);
        assertThat(newUser.getPassword()).isEqualTo(USER_PASSWORD1);
        assertThat(newUser.getEmail()).isEqualTo(USER_EMAIL1);
    }
}
