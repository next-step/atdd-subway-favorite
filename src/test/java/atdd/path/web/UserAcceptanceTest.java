package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.domain.entity.User;
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
        CreateUserRequestView view = CREATE_USER_REQUEST1;

        //when
        User user = userHttpTest.createUserRequest(view)
                .getResponseBody();

        //then
        assertThat(user.getName()).isEqualTo(USER_NAME1);
        assertThat(user.getEmail()).isEqualTo(USER_EMAIL1);
    }

    @Test
    public void deleteUser() {
        //given
        CreateUserRequestView view = CREATE_USER_REQUEST1;

        User user = userHttpTest.createUserRequest(view)
                .getResponseBody();

        //when
        //then
        userHttpTest.deleteUserRequest(user.getId());
    }

    @Test
    public void login() {
        //given
        CreateUserRequestView view = CREATE_USER_REQUEST1;

        User user = userHttpTest.createUserRequest(view)
                .getResponseBody();

        //when
        HttpHeaders headers = userHttpTest.loginRequest(LoginRequestView.builder()
                .name(user.getName())
                .password(USER_PASSWORD1).build()).getResponseHeaders();

        //then
        assertThat(headers.get("Authorization").size()).isEqualTo(1);
    }
}
