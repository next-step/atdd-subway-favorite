package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.UserSighUpResponseView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static atdd.path.fixture.UserFixture.*;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String KIM_INPUT_JSON = "{\"email\":\"" + KIM_EMAIL + "\",\"password\":\"" + KIM_PASSWORD + "\",\"name\":\"" + KIM_NAME + "\"}";;

    @DisplayName("유저_회원가입이_성공하는지")
    @Test
    public void userSighUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(KIM_INPUT_JSON), String.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().exists("Location")
                .expectBody()
                .jsonPath("$.email").isEqualTo(KIM_EMAIL)
                .jsonPath("$.name").isEqualTo(KIM_NAME);
    }


    @DisplayName("유저_회원_탈퇴가_성공하는지")
    @Test
    public void userDelete() {
        String createLocation = createUser();

        webTestClient.delete().uri(createLocation)
                .exchange()
                .expectStatus().isOk();
    }


    public String createUser() {
        return Objects.requireNonNull(webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(KIM_INPUT_JSON), String.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(UserSighUpResponseView.class)
                .getResponseHeaders()
                .getLocation())
                .getPath();
    }
}
