package atdd.user.application;

import atdd.user.application.dto.LoginResponseView;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.user.UserConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserService.class)
public class UserSeviceTest {
    private UserService userService;

    @MockBean
    private UserDao userDao;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(){
        this.userService = new UserService(userDao, jwtTokenProvider);
    }

    @DisplayName("로그인을 할 수 있다")
    @Test
    public void login(){
        User user = new User(USER_NAME, USER_PASSWORD, USER_EMAIL);
        given(userDao.save(user)).willReturn(user);

        LoginResponseView response = userService.logIn(USER_REQEUST_VIEW);

        assertThat(response.getAccessToken()).isNotEmpty();

    }

}
