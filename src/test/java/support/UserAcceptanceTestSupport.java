package support;

import atdd.path.application.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class UserAcceptanceTestSupport {
    @Autowired
    protected WebTestClient webTestClient;

    protected <T> EntityExchangeResult<T> getResource(String locationPath,
                                                      Class<T> responseBody) {

        return webTestClient.get()
                .uri(locationPath)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(responseBody)
                .returnResult();
    }

    protected UserResponseDto getUserResource(Long id) {
        return getResource("/users/" + id, UserResponseDto.class).getResponseBody();
    }

    private String createResource(String requestPath,
                                  Object requestDto) {

        EntityExchangeResult<Void> result = webTestClient.post()
                .uri(requestPath)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestDto), Object.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody(Void.class)
                .returnResult();

        return result.getResponseHeaders().getLocation().getPath();
    }

    protected Long createUserResource(Object requestDto) {
        return extractId(createResource("/users", requestDto));
    }

    protected Long extractId(String locationPath) {
        return Long.parseLong(locationPath.split("/")[2]);
    }

    private void deleteResource(String requestPath) {

        webTestClient.delete()
                .uri(requestPath)
                .exchange()
                .expectStatus().isOk();
    }

    public void deleteUserResource(Long id) {
        deleteResource("/users/" + id);
    }

}
