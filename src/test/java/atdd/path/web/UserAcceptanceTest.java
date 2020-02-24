package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.user.application.dto.CreateUserRequestView;
import atdd.user.application.dto.LoginRequestView;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @Test
    public void createUser() {
        //when
        CreateUserRequestView view = CREATE_USER_REQUEST2;
        User user = userHttpTest.createUserRequest(view)
                .getResponseBody();

        //then
        assertThat(user.getName()).isEqualTo(USER_NAME2);
        assertThat(user.getEmail()).isEqualTo(USER_EMAIL2);
    }

    @Test
    public void deleteUser() {
        //given
        User givenUser = userHttpTest.createUserRequest(CREATE_USER_REQUEST1).getResponseBody();
        String accessToken = userHttpTest.givenLogin(givenUser);

        CreateUserRequestView view = CREATE_USER_REQUEST2;
        User user = userHttpTest.createUserRequest(view)
                .getResponseBody();

        //when
        //then
        userHttpTest.deleteUserRequest(user.getId(), accessToken);
    }

    @Test
    public void login() {
        //given
        User givenUser = userHttpTest.createUserRequest(CREATE_USER_REQUEST1).getResponseBody();

        //when
        HttpHeaders headers = userHttpTest.loginRequest(LoginRequestView.builder()
                .email(givenUser.getEmail())
                .password(USER_PASSWORD1).build()).getResponseHeaders();

        //then
        assertThat(headers.get(ACCESS_TOKEN_HEADER).size()).isEqualTo(1);
    }

    @Test
    public void myInfo() {
        //given
        User givenUser = userHttpTest.createUserRequest(CREATE_USER_REQUEST1).getResponseBody();
        String accessToken = userHttpTest.givenLogin(givenUser);

        //when
        User user = userHttpTest.myInfoRequest(accessToken).getResponseBody();

        //then
        assertThat(user.getName()).isEqualTo(givenUser.getName());
        assertThat(user.getEmail()).isEqualTo(givenUser.getEmail());
    }
}
