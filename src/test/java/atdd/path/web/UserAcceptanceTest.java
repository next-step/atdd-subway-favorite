package atdd.path.web;

import atdd.path.AbstractAcceptanceTest;
import atdd.path.application.dto.StationResponseView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AbstractAcceptanceTest {
    public static final String USER_URL = "/users";

    @DisplayName("회원 가입")
    @Test
    public void createUser() {
        String inputJson = String.format("{\"email\": %s, \"name\": %s, \"password\": %s}", "boorwonie@email.com",
                "브라운", "subway");

        // when
        EntityExchangeResult<UserResponseView> userResponseAfterCreate = webTestClient.post()
                                   .uri(USER_URL)
                                   .contentType(MediaType.APPLICATION_JSON)
                                   .body(Mono.just(inputJson), String.class)
                                   .exchange()
                                   .expectStatus()
                                   .isCreated()
                                   .expectHeader()
                                   .contentType(MediaType.APPLICATION_JSON)
                                   .expectHeader()
                                   .exists("Location")
                                   .expectBody(UserResponseView.class)
                                   .returnResult();

        Long userId = userResponseAfterCreate.getId();

        // then
        EntityExchangeResult<List<StationResponseView>> userResponseAfterGet = webTestClient.get().uri(USER_URL)
                                                    .exchange()
                                                    .expectStatus().isOk()
                                                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                                                    .expectBodyList(UserResponseView.class)
                                                    .returnResult();

        assertThat(userResponseAfterGet.getResponseBody().get(0).getName()).isEqualTo("브라운");
    }
}
