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
import static atdd.path.TestConstant.*;

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
        User user = User.builder()
                .id(1l)
                .name(USER_NAME2)
                .email(USER_EMAIL2)
                .password(USER_ENCRYPT_PASSWORD2)
                .build();

        given(userDao.findByEmail(any())).willReturn(user);
        given(jwtUtils.createToken(any())).willReturn(any());

        String accessToken = userService.login(USER_EMAIL2, USER_PASSWORD2);
    }
}
