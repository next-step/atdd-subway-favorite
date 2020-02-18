package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static atdd.path.UserConstant.USER_NAME;
import static atdd.path.UserConstant.USER_PASSWORD;
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
                .build();

        UserResponseView userResponseView = webTestClient.post().uri("/stations")
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
    }
}
