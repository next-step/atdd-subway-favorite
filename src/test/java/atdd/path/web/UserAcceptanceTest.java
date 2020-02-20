package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.CreateUserRequestView;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.domain.entity.User;
import atdd.path.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    final String ACCESS_TOKEN_HEADER = "Authorization";

    @Autowired
    private UserRepository userRepository;

    private UserHttpTest userHttpTest;


    @BeforeEach
    void setUp() {
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @Test
    public void createUser() {
        //given
        User givenUser = givenUser(CREATE_USER_REQUEST1);
        String accessToken = givenAccessToken(givenUser);

        //when
        CreateUserRequestView view = CREATE_USER_REQUEST2;
        User user = userHttpTest.createUserRequest(view, accessToken)
                .getResponseBody();

        //then
        assertThat(user.getName()).isEqualTo(USER_NAME2);
        assertThat(user.getEmail()).isEqualTo(USER_EMAIL2);
    }

    @Test
    public void deleteUser() {
        //given
        User givenUser = givenUser(CREATE_USER_REQUEST1);
        String accessToken = givenAccessToken(givenUser);

        CreateUserRequestView view = CREATE_USER_REQUEST2;
        User user = userHttpTest.createUserRequest(view, accessToken)
                .getResponseBody();

        //when
        //then
        userHttpTest.deleteUserRequest(user.getId(), accessToken);
    }

    @Test
    public void login() {
        //given
        User givenUser = givenUser(CREATE_USER_REQUEST1);

        //when
        HttpHeaders headers = userHttpTest.loginRequest(LoginRequestView.builder()
                .email(givenUser.getEmail())
                .password(USER_PASSWORD1).build()).getResponseHeaders();

        //then
        assertThat(headers.get(ACCESS_TOKEN_HEADER).size()).isEqualTo(1);
    }

    private User givenUser(CreateUserRequestView view) {
        User user = view.toUSer();
        user.encryptPassword();

        return userRepository.save(user);
    }

    private String givenAccessToken(final User user) {
        HttpHeaders headers = userHttpTest.loginRequest(LoginRequestView.builder()
                .email(user.getEmail())
                .password(USER_PASSWORD1).build()).getResponseHeaders();

        return headers.get(ACCESS_TOKEN_HEADER).get(0);
    }

}
