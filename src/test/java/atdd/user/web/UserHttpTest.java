package atdd.user.web;

import atdd.user.application.dto.LoginResponseView;
import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.application.dto.UserResponseView;
import atdd.user.domain.User;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static atdd.user.UserConstant.*;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient){
        this.webTestClient = webTestClient;
    }

    public EntityExchangeResult<UserResponseView> createUserRequest(String name, String password,String email) {
        User user = User.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();

        return webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody(UserResponseView.class)
                .returnResult();
    }

    public Long createUser(String name, String password, String email){
        EntityExchangeResult<UserResponseView> userResponse = createUserRequest(name, password, email);
        return userResponse.getResponseBody().getId();
    }

    public LoginResponseView login(LoginUserRequestView login){
        //EntityExchangeResult<UserResponseView> creaetUser = createUserRequest(USER_NAME, USER_PASSWORD, USER_EMAIL);

        //LoginUserRequestView login =  new LoginUserRequestView(creaetUser.getResponseBody().getPassword(), creaetUser.getResponseBody().getEmail());
        LoginResponseView result = webTestClient.post().uri("/users/login")
                .body(Mono.just(login), LoginUserRequestView.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LoginResponseView.class)
                .returnResult()
                .getResponseBody();
        return result;
    }

}
