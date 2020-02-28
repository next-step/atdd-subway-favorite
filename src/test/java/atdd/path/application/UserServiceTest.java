package atdd.path.application;

import atdd.user.application.JwtUtils;
import atdd.user.application.UserService;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    public void login() {
        final String email = "test@gmail.com";
        final String encryptPassword = "$2a$10$QWeWyzF9vsyuVYQd.pHNtOj.Wshcu18yTeQ.5C7ti1lMJwgn788Qq";
        final String password = "rhkwprkalffuTekdk";

        User user = User.builder()
                .id(1l)
                .email(email)
                .name("ㅠㅠ")
                .password(encryptPassword).build();

        given(userDao.findByEmail(any())).willReturn(user);
        given(jwtUtils.createToken(any())).willReturn("test");

        String accessToken = userService.login(email, password);
    }
}
