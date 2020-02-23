package atdd.user.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.user.application.dto.LoginResponseView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import static atdd.user.UserConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String USER_URL = "/users";

    private UserHttpTest userHttpTest;

    @BeforeEach
    void setUp(){
        this.userHttpTest = new UserHttpTest(webTestClient);
    }

    @DisplayName("사용자를 등록한다")
    @Test
    public void createUser(){
        User user = User.builder()
                .name(USER_NAME)
                .password(USER_PASSWORD)
                .email(USER_EMAIL)
                .build();

        UserResponseView userResponseView = webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(UserResponseView.class)
                .returnResult()
                .getResponseBody();
        assertThat(userResponseView.getName()).isEqualTo(USER_NAME);
        assertThat(userResponseView.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(userResponseView.getEmail()).isEqualTo(USER_EMAIL);
    }

    @DisplayName("사용자를 삭제한다")
    @Test
    public void deleteUser(){
        EntityExchangeResult<UserResponseView> response = userHttpTest.createUserRequest(USER_NAME, USER_PASSWORD, USER_EMAIL );

        webTestClient.delete().uri("users/"+response.getResponseBody().getId())
                .exchange()
                .expectStatus().isNoContent();

    }

    @DisplayName("로그인 요청을 한다")
    @Test
    public void login(){
        EntityExchangeResult<UserResponseView> response = userHttpTest.createUserRequest(USER_NAME, USER_PASSWORD, USER_EMAIL );

        LoginUserRequestView request =  new LoginUserRequestView(response.getResponseBody().getPassword(), response.getResponseBody().getEmail());
        LoginResponseView result = webTestClient.post().uri("/users/login")
                .body(Mono.just(request), LoginUserRequestView.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LoginResponseView.class)
                .returnResult()
                .getResponseBody();

        assertThat(result.getAccessToken()).isNotEmpty();

    }

}
