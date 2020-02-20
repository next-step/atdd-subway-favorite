package atdd.user.acceptance;

import atdd.user.controller.UserController;
import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static atdd.user.controller.UserController.*;

public class UserHttpTestSupport {

    private final WebTestClient webTestClient;

    public UserHttpTestSupport(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public UserResponseDto create(UserCreateRequestDto requestDto) {
        return webTestClient.post()
                .uri(ROOT_URI)
                .body(Mono.just(requestDto), UserCreateRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();
    }

}
