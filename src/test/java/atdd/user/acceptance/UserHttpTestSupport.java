package atdd.user.acceptance;

import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static atdd.user.controller.UserController.ROOT_URI;

public class UserHttpTestSupport {

    private final WebTestClient webTestClient;

    public UserHttpTestSupport(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public String makeRequestUri(String uri, MultiValueMap<String, String> params, Object... uriVariables) {
        return UriComponentsBuilder.fromUriString(uri).queryParams(params).build(uriVariables).toString();
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
