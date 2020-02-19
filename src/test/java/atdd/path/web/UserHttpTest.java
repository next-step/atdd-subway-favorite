package atdd.path.web;

import atdd.path.application.dto.UserResponseView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class UserHttpTest {
    public WebTestClient webTestClient;

    public UserHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    private EntityExchangeResult<UserResponseView> createUserTest(String reqUri, String inputJson) {
        return webTestClient
                .post()
                .uri(reqUri)
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
                .consumeWith(result -> {
                    HttpHeaders responseHeaders = result.getResponseHeaders();
                    URI location = responseHeaders.getLocation();
                    String stringifyLocation = location.toString();
                    assertThat(stringifyLocation).isEqualTo(reqUri + "/" + result.getResponseBody().getId());
                })
                .returnResult();
    }

    public Long createUserSuccess(String reqUri, String inputJson) {
        EntityExchangeResult<UserResponseView> responseEntity = createUserTest(reqUri, inputJson);
        return responseEntity.getResponseBody().getId();
    }

    public WebTestClient.ResponseSpec showUsersAndSingleUserTest(String reqUri) {
        return webTestClient
                .get()
                .uri(reqUri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON);
    }
}
