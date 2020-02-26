package atdd.path.application;

import atdd.user.application.UserService;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @Test
    public void login() {
        final String email = "test@gmail.com";
        final String password = "rhkwprkalffuTekdk";

        User user = User.builder()
                .id(1l)
                .email(email)
                .name("ㅠㅠ")
                .password(password).build();

        given(userDao.findByEmail(any())).willReturn(user);

        String accessToken = userService.login(email, password);

        assertThat(accessToken.length()).isGreaterThan(0);
    }
}
